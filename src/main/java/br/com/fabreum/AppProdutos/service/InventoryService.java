package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.model.*;
import br.com.fabreum.AppProdutos.repository.EstoqueRepository;
import br.com.fabreum.AppProdutos.repository.InventoryTransactionRepository;
import br.com.fabreum.AppProdutos.repository.ProdutosRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryTransactionRepository transactionRepository;
    private final EstoqueRepository estoqueRepository;
    private final ProdutosRepository produtosRepository;

    @Transactional
    public void processTransaction(Long productId, Integer quantity, TransactionType reason) {
        Produtos produto = produtosRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        InventoryTransaction tx = new InventoryTransaction();
        tx.setProduto(produto);
        tx.setQuantity(quantity);
        tx.setReason(reason);
        transactionRepository.save(tx);

        Estoque estoque = estoqueRepository.findByProdutoId(productId)
                .orElseGet(() -> {
                    Estoque novo = new Estoque();
                    novo.setProduto(produto);
                    novo.setQuantidade(0);
                    return novo;
                });

        int novoSaldo = estoque.getQuantidade() + quantity;

        if (novoSaldo < 0) {
            throw new RuntimeException("Estoque insuficiente! Saldo atual: " + estoque.getQuantidade());
        }

        estoque.setQuantidade(novoSaldo);
        estoqueRepository.save(estoque);
    }

    public Integer getSaldoAtual(Long productId) {
        return estoqueRepository.findByProdutoId(productId)
                .map(Estoque::getQuantidade)
                .orElse(0);
    }
}