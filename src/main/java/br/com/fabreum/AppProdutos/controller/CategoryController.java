package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.model.Category;
import br.com.fabreum.AppProdutos.repository.CategoryRepository;
import br.com.fabreum.AppProdutos.service.CategoryService;
import br.com.fabreum.AppProdutos.service.dto.ApiResponseDto;
import br.com.fabreum.AppProdutos.service.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;
    private final CategoryRepository repository;

    @GetMapping("listar")
    public ResponseEntity<ApiResponseDto<List<Category>>> listarTodas() {
        return ResponseEntity.ok(new ApiResponseDto<>("Categorias listadas.", service.findAll()));
    }

    @GetMapping("listar/{id}")
    public ResponseEntity<ApiResponseDto<Category>> buscarPorId(@PathVariable Long id) {
        return service.findById(id)
                .map(c -> ResponseEntity.ok(new ApiResponseDto<>("Categoria encontrada.", c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<ApiResponseDto<CategoryDto>> buscarDtoPorId(@PathVariable Long id) {
        CategoryDto dto = repository.findByIdDto(id);
        if (dto != null) {
            return ResponseEntity.ok(new ApiResponseDto<>("Detalhes da categoria.", dto));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("criar")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Category>> criar(@RequestBody Category category) {
        Category nova = service.save(category);
        return ResponseEntity.ok(new ApiResponseDto<>("Categoria criada com sucesso!", nova));
    }

    @PutMapping("atualizar/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Category>> atualizar(@PathVariable Long id, @RequestBody Category category) {
        return service.update(id, category)
                .map(c -> ResponseEntity.ok(new ApiResponseDto<>("Categoria atualizada.", c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("deletar/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> deletar(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.ok(new ApiResponseDto<>("Categoria excluída com sucesso!"));
        }
        return ResponseEntity.notFound().build();
    }
}