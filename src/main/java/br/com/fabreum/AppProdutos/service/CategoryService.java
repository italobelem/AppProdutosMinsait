package br.com.fabreum.AppProdutos.service;

import br.com.fabreum.AppProdutos.model.Category;
import br.com.fabreum.AppProdutos.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Optional<Category> findById(Long id) {
        return repository.findById(id);
    }

    public Category save(Category category) {
        return repository.save(category);
    }

    public Optional<Category> update(Long id, Category novosDados) {
        return repository.findById(id).map(categoriaExistente -> {
            categoriaExistente.setName(novosDados.getName());

            if (novosDados.getParent() != null) {
                categoriaExistente.setParent(novosDados.getParent());
            }

            return repository.save(categoriaExistente);
        });
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}