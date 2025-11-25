package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.service.CartService;
import br.com.fabreum.AppProdutos.service.dto.ApiResponseDto;
import br.com.fabreum.AppProdutos.service.dto.CartItemDto;
import br.com.fabreum.AppProdutos.service.dto.CartResponseDto;
import br.com.fabreum.AppProdutos.service.dto.CartUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/cart")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
@Tag(name = "Carrinho de Compras", description = "Gestão de itens do carrinho (Exclusivo para Clientes)")
@SecurityRequirement(name = "bearer-key")
public class CartController {

    private final CartService service;

    @Operation(summary = "Ver meu carrinho", description = "Retorna os itens e o total do carrinho atual do cliente.")
    @GetMapping("/listarItens")
    public ResponseEntity<ApiResponseDto<CartResponseDto>> getMyCart() {
        return ResponseEntity.ok(new ApiResponseDto<>("Seu carrinho de compras.", service.getMyCart()));
    }

    @Operation(summary = "Adicionar item", description = "Adiciona um produto ao carrinho. Se já existir, soma a quantidade.")
    @PostMapping("/add")
    public ResponseEntity<ApiResponseDto<CartResponseDto>> addItem(@RequestBody @Valid CartItemDto dto) {
        return ResponseEntity.ok(new ApiResponseDto<>("Item adicionado ao carrinho!", service.addItem(dto)));
    }

    @Operation(summary = "Atualizar quantidade", description = "Altera a quantidade de um item específico no carrinho.")
    @PutMapping("atualizar/{itemId}")
    public ResponseEntity<ApiResponseDto<CartResponseDto>> updateItem(
            @PathVariable Long itemId,
            @RequestBody @Valid CartUpdateDto dto) {
        return ResponseEntity.ok(new ApiResponseDto<>("Quantidade atualizada.", service.updateItemQuantity(itemId, dto.quantity())));
    }

    @Operation(summary = "Remover item", description = "Remove um item do carrinho.")
    @DeleteMapping("deletar/{itemId}")
    public ResponseEntity<ApiResponseDto<CartResponseDto>> removeItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(new ApiResponseDto<>("Item removido do carrinho.", service.removeItem(itemId)));
    }
}