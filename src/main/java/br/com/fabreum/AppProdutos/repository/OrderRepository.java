package br.com.fabreum.AppProdutos.repository;

import br.com.fabreum.AppProdutos.model.Order;
import br.com.fabreum.AppProdutos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}