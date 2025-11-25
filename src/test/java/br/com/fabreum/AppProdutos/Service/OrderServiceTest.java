package br.com.fabreum.AppProdutos.Service;

import br.com.fabreum.AppProdutos.model.*;
import br.com.fabreum.AppProdutos.repository.CartRepository;
import br.com.fabreum.AppProdutos.repository.OrderRepository;
import br.com.fabreum.AppProdutos.repository.UserRepository;
import br.com.fabreum.AppProdutos.service.InventoryService;
import br.com.fabreum.AppProdutos.service.OrderService;
import br.com.fabreum.AppProdutos.service.dto.OrderResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private InventoryService inventoryService;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    private User user;
    private Cart cart;
    private Produtos produto;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId("user-123");
        user.setLogin("cliente@teste.com");

        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn("cliente@teste.com");
        SecurityContextHolder.setContext(securityContext);

        produto = new Produtos();
        produto.setId(1L);
        produto.setNome("Teste Phone");
        produto.setPreco(new BigDecimal("1000.00"));

        cart = new Cart();
        cart.setUser(user);
        cart.setStatus(CartStatus.OPEN);
    }

    @Test
    @DisplayName("Checkout deve criar pedido e baixar stock quando carrinho tem itens")
    void checkout_Success() {
        CartItem item = new CartItem();
        item.setProduto(produto);
        item.setQuantity(2);
        item.setPriceSnapshot(new BigDecimal("1000.00")); // 2x 1000 = 2000
        cart.setItems(List.of(item));

        when(userRepository.findByLogin("cliente@teste.com")).thenReturn(user);
        when(cartRepository.findByUserAndStatus(user, CartStatus.OPEN)).thenReturn(Optional.of(cart));

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
            Order o = i.getArgument(0);
            o.setId(1L);
            return o;
        });

        OrderResponseDto response = orderService.checkout();

        assertNotNull(response);
        assertEquals(new BigDecimal("2000.00"), response.total());
        assertEquals("CREATED", response.status());

        verify(inventoryService, times(1)).processTransaction(1L, -2, TransactionType.SALE);

        assertEquals(CartStatus.CLOSED, cart.getStatus());
    }

    @Test
    @DisplayName("Checkout deve lançar erro se carrinho estiver vazio")
    void checkout_EmptyCart() {
        cart.setItems(List.of());

        when(userRepository.findByLogin(anyString())).thenReturn(user);
        when(cartRepository.findByUserAndStatus(any(), any())).thenReturn(Optional.of(cart));

        assertThrows(RuntimeException.class, () -> orderService.checkout());

        verify(inventoryService, never()).processTransaction(anyLong(), anyInt(), any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Cancelamento deve estornar stock se status for CREATED")
    void cancelOrder_Success() {
        Order order = new Order();
        order.setId(5L);
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);

        order.setTotal(new BigDecimal("2000.00"));

        OrderItem orderItem = new OrderItem();
        orderItem.setProduto(produto);
        orderItem.setQuantity(2);

        orderItem.setPrice(new BigDecimal("1000.00"));

        order.setItems(List.of(orderItem));

        when(userRepository.findByLogin(anyString())).thenReturn(user);
        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(5L);

        assertEquals(OrderStatus.CANCELED, order.getStatus());

        verify(inventoryService).processTransaction(1L, 2, TransactionType.RETURN);
    }
}