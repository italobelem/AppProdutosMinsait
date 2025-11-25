package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.model.*;
import br.com.fabreum.AppProdutos.repository.CartRepository;
import br.com.fabreum.AppProdutos.repository.OrderRepository;
import br.com.fabreum.AppProdutos.repository.UserRepository;
import br.com.fabreum.AppProdutos.service.dto.OrderResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;

    private User getLoggedUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) userRepository.findByLogin(auth.getName());
    }

    @Transactional
    public OrderResponseDto checkout() {
        User user = getLoggedUser();

        Cart cart = cartRepository.findByUserAndStatus(user, CartStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("Não há carrinho aberto para finalizar."));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("O carrinho está vazio.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);
        order.setTotal(BigDecimal.ZERO);

        BigDecimal totalOrder = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduto(cartItem.getProduto());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPriceSnapshot());

            order.getItems().add(orderItem);

            totalOrder = totalOrder.add(cartItem.getSubTotal());

            inventoryService.processTransaction(
                    cartItem.getProduto().getId(),
                    -cartItem.getQuantity(),
                    TransactionType.SALE
            );
        }

        order.setTotal(totalOrder);
        orderRepository.save(order);

        cart.setStatus(CartStatus.CLOSED);
        cartRepository.save(cart);

        return OrderResponseDto.fromEntity(order);
    }

    @Transactional
    public OrderResponseDto cancelOrder(Long orderId) {
        User user = getLoggedUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Você não tem permissão para cancelar este pedido.");
        }
        if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.PAID) {
            throw new RuntimeException("Não é possível cancelar. Status atual: " + order.getStatus());
        }

        for (OrderItem item : order.getItems()) {
            inventoryService.processTransaction(
                    item.getProduto().getId(),
                    item.getQuantity(),
                    TransactionType.RETURN
            );
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        return OrderResponseDto.fromEntity(order);
    }

    public List<OrderResponseDto> getMyOrders() {
        User user = getLoggedUser();
        return orderRepository.findByUser(user).stream()
                .map(OrderResponseDto::fromEntity)
                .toList();
    }
}