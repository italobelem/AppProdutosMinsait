package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.repository.ProdutosRepository;
import br.com.fabreum.AppProdutos.service.ProdutosService;
import br.com.fabreum.AppProdutos.service.dto.ApiResponseDto;
import br.com.fabreum.AppProdutos.service.dto.ProdutoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/produtos")
@Tag(name = "Produtos", description = "Catálogo de produtos da loja")
@SecurityRequirement(name = "bearer-key")
public class ProdutoController {

    private final ProdutosRepository produtosRepository;
    private final ProdutosService produtosService;

    @Operation(summary = "Criar Produto", description = "Cadastra um novo produto. (Admin/Seller)")
    @PostMapping("/criar")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SELLER')")
    public ResponseEntity<ApiResponseDto<Produtos>> criaProduto(@RequestBody @Valid Produtos produto) {
        Produtos saved = produtosRepository.save(produto);
        return ResponseEntity.ok(new ApiResponseDto<>("Produto criado com sucesso!", saved));
    }

    @Operation(summary = "Listar Produtos", description = "Retorna a lista completa de produtos.")
    @GetMapping("/listar")
    public ResponseEntity<ApiResponseDto<List<Produtos>>> listaProdutos() {
        return ResponseEntity.ok(new ApiResponseDto<>("Lista de produtos recuperada.", produtosRepository.findAll()));
    }

    @Operation(summary = "Buscar por ID", description = "Retorna detalhes de um produto específico.")
    @GetMapping("/listar/{id}")
    public ResponseEntity<ApiResponseDto<Produtos>> listaProdutoPorId(@PathVariable Long id) {
        Produtos produto = produtosRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return ResponseEntity.ok(new ApiResponseDto<>("Produto encontrado.", produto));
    }

    @Operation(summary = "Buscar DTO Simplificado", description = "Retorna um objeto leve (DTO) do produto.")
    @GetMapping("/listarSimplificado/{id}")
    public ResponseEntity<ApiResponseDto<ProdutoDto>> listaProdutoDtoPorId(@PathVariable Long id) {
        ProdutoDto produtoDto = produtosRepository.findByIdDto(id);
        if (produtoDto == null) throw new RuntimeException("Produto não encontrado");
        return ResponseEntity.ok(new ApiResponseDto<>("Detalhes simplificados do produto.", produtoDto));
    }

    @Operation(summary = "Atualizar Produto", description = "Atualiza dados de um produto existente. (Admin/Seller)")
    @PutMapping("/atualizar/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SELLER')")
    public ResponseEntity<ApiResponseDto<Produtos>> atualizaProduto(@PathVariable Long id, @RequestBody @Valid Produtos produto) {
        Optional<Produtos> atualizado = produtosService.atualizaProduto(id, produto);
        return atualizado
                .map(p -> ResponseEntity.ok(new ApiResponseDto<>("Produto atualizado com sucesso!", p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deletar Produto", description = "Remove um produto do catálogo. (Apenas Admin)")
    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> deletaProduto(@PathVariable Long id) {

        if (!produtosRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado com o ID informado.");
        }

        produtosRepository.deleteById(id);

        return ResponseEntity.ok(new ApiResponseDto<>("Produto removido com sucesso!"));
    }
}