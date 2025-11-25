package br.com.fabreum.AppProdutos.service.dto;

import br.com.fabreum.AppProdutos.model.Cart;
import br.com.fabreum.AppProdutos.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

public record CartResponseDto(
        Long cartId,
        List<ItemView> items,
        BigDecimal total
) {
    public record ItemView(Long productId, String productName, Integer quantity, BigDecimal unitPrice, BigDecimal subTotal) {}

    public static CartResponseDto fromEntity(Cart cart) {
        BigDecimal totalCart = BigDecimal.ZERO;

        List<ItemView> itemViews = cart.getItems().stream().map(item ->
                new ItemView(
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getQuantity(),
                        item.getPriceSnapshot(),
                        item.getSubTotal()
                )
        ).toList();

        for (CartItem item : cart.getItems()) {
            totalCart = totalCart.add(item.getSubTotal());
        }

        return new CartResponseDto(cart.getId(), itemViews, totalCart);
    }
}