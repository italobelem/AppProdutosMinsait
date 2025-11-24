package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.repository.ProdutosRepository;
import br.com.fabreum.AppProdutos.service.ProdutosService;
import br.com.fabreum.AppProdutos.service.dto.ProdutoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/produtos")
public class ProdutoController {

    private final ProdutosRepository produtosRepository;
    private final ProdutosService produtosService;

    @GetMapping("/me")
    public ResponseEntity<String> quemSouEu() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Usuário: " + auth.getName() + " | Permissões: " + auth.getAuthorities());
    }

    @GetMapping("/meu-perfil-de-seguranca")
    public ResponseEntity<String> debugSeguranca() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(
                "Usuário Logado: " + auth.getName() + "\n" +
                        "Permissões Carregadas: " + auth.getAuthorities()
        );
    }

    @PostMapping("/criar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<Produtos> criaProduto(@RequestBody Produtos produto) {
        Produtos saved = produtosRepository.save(produto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/listar")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<List<Produtos>> listaProdutos() {
        List<Produtos> produtos = produtosRepository.findAll();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<Produtos> listaProdutoPorId(@PathVariable Long id) {
        Produtos produto = produtosRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/listarSimplificado/{id}")
    public ResponseEntity<ProdutoDto> listaProdutoDtoPorId(@PathVariable Long id) {
        ProdutoDto produtoDto = produtosRepository.findByIdDto(id);

        final var produto = new Produtos();
        produto.setNome(produtoDto.nome());
        produto.setPreco(produtoDto.preco());
        produto.setCodigoBarras(produtoDto.codigoBarras());
        produtosRepository.saveAndFlush(produto);

        return ResponseEntity.ok(produtoDto);
    }

    @PutMapping("/atualizar/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<String> atualizaProduto(@PathVariable Long id, @RequestBody Produtos produto) {

        if (produtosRepository.findByIdDto(id) != null) {

            produtosService.atualizaProduto(id, produto);

            return ResponseEntity.ok("Produto com o ID: " + id + " foi alterado com sucesso");
        }return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Não foi possível encontrar nenhum produto com o ID: " + id + " para ser atualizado");
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Void> deletaProduto(@PathVariable Long id) {
        produtosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
