package br.com.fabreum.AppProdutos.Service;

import br.com.fabreum.AppProdutos.model.Estoque;
import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.model.TransactionType;
import br.com.fabreum.AppProdutos.repository.EstoqueRepository;
import br.com.fabreum.AppProdutos.repository.InventoryTransactionRepository;
import br.com.fabreum.AppProdutos.repository.ProdutosRepository;
import br.com.fabreum.AppProdutos.service.InventoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private EstoqueRepository estoqueRepository;
    @Mock
    private ProdutosRepository produtosRepository;
    @Mock
    private InventoryTransactionRepository transactionRepository;

    @Test
    @DisplayName("Deve BLOQUEAR venda se não houver saldo suficiente")
    void processTransaction_InsufficientStock() {
        Long productId = 1L;
        Produtos p = new Produtos();
        p.setId(productId);

        Estoque estoqueAtual = new Estoque();
        estoqueAtual.setQuantidade(5);
        estoqueAtual.setProduto(p);

        when(produtosRepository.findById(productId)).thenReturn(Optional.of(p));
        when(estoqueRepository.findByProdutoId(productId)).thenReturn(Optional.of(estoqueAtual));

        assertThrows(RuntimeException.class, () ->
                inventoryService.processTransaction(productId, -10, TransactionType.SALE)
        );

        verify(estoqueRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve PERMITIR transação se saldo for suficiente")
    void processTransaction_Success() {
        Long productId = 1L;
        Produtos p = new Produtos();
        p.setId(productId);

        Estoque estoqueAtual = new Estoque();
        estoqueAtual.setQuantidade(10);
        estoqueAtual.setProduto(p);

        when(produtosRepository.findById(productId)).thenReturn(Optional.of(p));
        when(estoqueRepository.findByProdutoId(productId)).thenReturn(Optional.of(estoqueAtual));

        inventoryService.processTransaction(productId, -5, TransactionType.SALE);

        verify(estoqueRepository).save(argThat(estoque -> estoque.getQuantidade() == 5));
        verify(transactionRepository).save(any());
    }
}