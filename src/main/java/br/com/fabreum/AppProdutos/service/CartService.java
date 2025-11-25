package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.model.*;
import br.com.fabreum.AppProdutos.repository.CartRepository;
import br.com.fabreum.AppProdutos.repository.ProdutosRepository;
import br.com.fabreum.AppProdutos.repository.UserRepository;
import br.com.fabreum.AppProdutos.service.dto.CartItemDto;
import br.com.fabreum.AppProdutos.service.dto.CartResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProdutosRepository produtosRepository;
    private final UserRepository userRepository;

    private User getLoggedUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var login = auth.getName();
        return (User) userRepository.findByLogin(login);
    }

    public CartResponseDto getMyCart() {
        User user = getLoggedUser();
        Cart cart = getOrCreateCart(user);
        return CartResponseDto.fromEntity(cart);
    }

    @Transactional
    public CartResponseDto addItem(CartItemDto dto) {
        User user = getLoggedUser();
        Cart cart = getOrCreateCart(user);
        Produtos produto = produtosRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Optional<CartItem> itemExistente = cart.getItems().stream()
                .filter(item -> item.getProduto().getId().equals(produto.getId()))
                .findFirst();

        if (itemExistente.isPresent()) {
            CartItem item = itemExistente.get();
            item.setQuantity(item.getQuantity() + dto.quantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduto(produto);
            newItem.setQuantity(dto.quantity());
            newItem.setPriceSnapshot(produto.getPreco()); // <--- AQUI ESTÁ A REGRA DE OURO

            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
        return CartResponseDto.fromEntity(cart);
    }



    @Transactional
    public CartResponseDto updateItemQuantity(Long itemId, Integer novaQuantidade) {
        User user = getLoggedUser();
        Cart cart = getOrCreateCart(user);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item não encontrado no seu carrinho"));

        item.setQuantity(novaQuantidade);

        cartRepository.save(cart);
        return CartResponseDto.fromEntity(cart);
    }

    @Transactional
    public CartResponseDto removeItem(Long itemId) {
        User user = getLoggedUser();
        Cart cart = getOrCreateCart(user);

        CartItem itemToRemove = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item não encontrado no seu carrinho"));

        cart.getItems().remove(itemToRemove);

        cartRepository.save(cart);
        return CartResponseDto.fromEntity(cart);
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUserAndStatus(user, CartStatus.OPEN)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setStatus(CartStatus.OPEN);
                    return cartRepository.save(newCart);
                });
    }
}