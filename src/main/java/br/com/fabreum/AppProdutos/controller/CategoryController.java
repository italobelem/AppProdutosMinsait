package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.model.Category;
import br.com.fabreum.AppProdutos.repository.CategoryRepository;
import br.com.fabreum.AppProdutos.service.CategoryService;
import br.com.fabreum.AppProdutos.service.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/categorias")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;
    private final CategoryRepository repository;

    @GetMapping("listar")
    public ResponseEntity<List<Category>> listarTodas() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("listar/{id}")
    public ResponseEntity<Category> buscarPorId(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<CategoryDto> buscarDtoPorId(@PathVariable Long id) {
        CategoryDto dto = repository.findByIdDto(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("criar")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Category> criar(@RequestBody Category category) {
        Category nova = service.save(category);
        return ResponseEntity.ok(nova);
    }

    @PutMapping("atualizar/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Category> atualizar(@PathVariable Long id, @RequestBody Category category) {
        return service.update(id, category)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("deletar/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}