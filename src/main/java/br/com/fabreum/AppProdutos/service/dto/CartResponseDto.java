package br.com.fabreum.AppProdutos.service.dto;

import br.com.fabreum.AppProdutos.model.Cart;
import br.com.fabreum.AppProdutos.model.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

public record CartResponseDto(
        @Schema(description = "ID do carrinho", example = "1")
        Long cartId,

        @Schema(description = "Lista de itens no carrinho")
        List<ItemView> items,

        @Schema(description = "Valor total do carrinho", example = "4500.00")
        BigDecimal total
) {
    public record ItemView(
            @Schema(example = "10") Long productId,
            @Schema(example = "PlayStation 5") String productName,
            @Schema(example = "1") Integer quantity,
            @Schema(example = "4500.00") BigDecimal unitPrice,
            @Schema(example = "4500.00") BigDecimal subTotal
    ) {}

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