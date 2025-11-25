package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.model.TransactionType;
import br.com.fabreum.AppProdutos.service.InventoryService;
import br.com.fabreum.AppProdutos.service.dto.ApiResponseDto;
import br.com.fabreum.AppProdutos.service.dto.StockDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SELLER')")
    public ResponseEntity<ApiResponseDto<Void>> addStock(@RequestBody @Valid StockDto dto) {
        service.processTransaction(dto.productId(), dto.quantity(), TransactionType.PURCHASE);
        return ResponseEntity.ok(new ApiResponseDto<>("Estoque adicionado com sucesso."));
    }

    @PostMapping("/remove")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> removeStock(@RequestBody @Valid StockDto dto) {
        service.processTransaction(dto.productId(), -dto.quantity(), TransactionType.ADJUSTMENT);
        return ResponseEntity.ok(new ApiResponseDto<>("Estoque removido (ajuste manual)."));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponseDto<Integer>> getBalance(@PathVariable Long productId) {
        Integer saldo = service.getSaldoAtual(productId);
        return ResponseEntity.ok(new ApiResponseDto<>("Saldo atual consultado.", saldo));
    }
}