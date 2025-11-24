package br.com.fabreum.AppProdutos.repository;

import br.com.fabreum.AppProdutos.model.Produtos;
import br.com.fabreum.AppProdutos.service.dto.ProdutoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, Long> {

    @Query("""
            SELECT new br.com.fabreum.AppProdutos.service.dto.ProdutoDto(
                p.id, 
                p.codigoBarras, 
                p.nome, 
                p.preco
            )
            FROM Produtos p 
            WHERE p.id = :id
            """)
    ProdutoDto findByIdDto(Long id);

}