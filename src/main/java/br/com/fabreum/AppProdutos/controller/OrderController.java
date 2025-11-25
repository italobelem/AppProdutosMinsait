package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.service.OrderService;
import br.com.fabreum.AppProdutos.service.dto.ApiResponseDto;
import br.com.fabreum.AppProdutos.service.dto.OrderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/orders")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Fluxo de checkout e histórico de pedidos")
@SecurityRequirement(name = "bearer-key")
public class OrderController {

    private final OrderService service;

    @Operation(summary = "Finalizar Pedido (Checkout)", description = "Transforma o carrinho atual do cliente em um pedido e baixa o estoque.")
    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> checkout() {
        OrderResponseDto order = service.checkout();
        return ResponseEntity.ok(new ApiResponseDto<>("Pedido realizado com sucesso! ID: " + order.orderId(), order));
    }

    @Operation(summary = "Listar meus pedidos", description = "Lista o histórico de compras do cliente logado.")
    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ApiResponseDto<List<OrderResponseDto>>> getMyOrders() {
        return ResponseEntity.ok(new ApiResponseDto<>("Seus pedidos.", service.getMyOrders()));
    }

    @Operation(summary = "Cancelar Pedido", description = "Cancela um pedido (se status permitir) e estorna o estoque.")
    @PostMapping("/cancel/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponseDto<>("Pedido cancelado com sucesso. O estoque foi estornado.", service.cancelOrder(id)));
    }
}