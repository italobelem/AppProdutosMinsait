package br.com.fabreum.AppProdutos.service.dto;

import br.com.fabreum.AppProdutos.model.Order;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        @Schema(description = "Número do pedido", example = "123")
        Long orderId,

        @Schema(description = "Status atual", example = "CREATED")
        String status,

        @Schema(description = "Valor total pago", example = "4500.00")
        BigDecimal total,

        @Schema(description = "Data da compra")
        LocalDateTime createdAt,

        List<OrderItemDTO> items
) {
    public record OrderItemDTO(
            @Schema(example = "PlayStation 5") String productName,
            @Schema(example = "1") Integer quantity,
            @Schema(example = "4500.00") BigDecimal unitPrice,
            @Schema(example = "4500.00") BigDecimal subTotal
    ) {}

    public static OrderResponseDto fromEntity(Order order) {
        List<OrderItemDTO> items = order.getItems().stream()
                .map(i -> new OrderItemDTO(
                        i.getProduto().getNome(),
                        i.getQuantity(),
                        i.getPrice(),
                        i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()))
                )).toList();

        return new OrderResponseDto(
                order.getId(),
                order.getStatus().name(),
                order.getTotal(),
                order.getCreatedAt(),
                items
        );
    }
}