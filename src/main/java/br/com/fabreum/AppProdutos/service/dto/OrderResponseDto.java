package br.com.fabreum.AppProdutos.service.dto;

import br.com.fabreum.AppProdutos.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long orderId,
        String status,
        BigDecimal total,
        LocalDateTime createdAt,
        List<OrderItemDTO> items
) {
    public record OrderItemDTO(String productName, Integer quantity, BigDecimal unitPrice, BigDecimal subTotal) {}

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