package br.com.fabreum.AppProdutos.repository;

import br.com.fabreum.AppProdutos.model.Category;
import br.com.fabreum.AppProdutos.service.dto.CategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
           SELECT new br.com.fabreum.AppProdutos.service.dto.CategoryDto(
               c.id,
               c.name,
               c.parent.name
           )
           FROM Category c
           LEFT JOIN c.parent
           WHERE c.id = :id
           """)
    CategoryDto findByIdDto(Long id);
}