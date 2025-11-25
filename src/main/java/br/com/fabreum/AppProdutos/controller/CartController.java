package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.service.CartService;
import br.com.fabreum.AppProdutos.service.dto.CartItemDto;
import br.com.fabreum.AppProdutos.service.dto.CartResponseDto;
import br.com.fabreum.AppProdutos.service.dto.CartUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    private final CartService service;

    @GetMapping("/listar")
    public ResponseEntity<CartResponseDto> getMyCart() {
        return ResponseEntity.ok(service.getMyCart());
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addItem(@RequestBody @Valid CartItemDto dto) {
        return ResponseEntity.ok(service.addItem(dto));
    }

    @PutMapping("/alterar/{itemId}")
    public ResponseEntity<CartResponseDto> updateItem(
            @PathVariable Long itemId,
            @RequestBody @Valid CartUpdateDto dto) {
        return ResponseEntity.ok(service.updateItemQuantity(itemId, dto.quantity()));
    }

    @DeleteMapping("/deletar/{itemId}")
    public ResponseEntity<CartResponseDto> removeItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(service.removeItem(itemId));
    }
}