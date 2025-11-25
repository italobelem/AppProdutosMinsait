package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.service.OrderService;
import br.com.fabreum.AppProdutos.service.dto.ApiResponseDto;
import br.com.fabreum.AppProdutos.service.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> checkout() {
        OrderResponseDto order = service.checkout();
        return ResponseEntity.ok(new ApiResponseDto<>("Pedido realizado com sucesso! ID: " + order.orderId(), order));
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ApiResponseDto<List<OrderResponseDto>>> getMyOrders() {
        return ResponseEntity.ok(new ApiResponseDto<>("Seus pedidos.", service.getMyOrders()));
    }

    @PostMapping("/cancel/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponseDto<>("Pedido cancelado com sucesso. O estoque foi estornado.", service.cancelOrder(id)));
    }
}