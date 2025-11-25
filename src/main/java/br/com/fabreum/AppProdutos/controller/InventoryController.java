package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.model.TransactionType;
import br.com.fabreum.AppProdutos.service.InventoryService;
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
    public ResponseEntity<String> addStock(@RequestBody @Valid StockDto dto) {
        service.processTransaction(dto.productId(), dto.quantity(), TransactionType.PURCHASE);
        return ResponseEntity.ok("Estoque adicionado com sucesso.");
    }

    @PostMapping("/remove")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> removeStock(@RequestBody @Valid StockDto dto) {
        service.processTransaction(dto.productId(), -dto.quantity(), TransactionType.ADJUSTMENT);
        return ResponseEntity.ok("Estoque removido com sucesso.");
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Integer> getBalance(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getSaldoAtual(productId));
    }
}