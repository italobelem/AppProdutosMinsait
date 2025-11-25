package br.com.fabreum.AppProdutos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_inventory_transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Produtos produto;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private TransactionType reason;

    @CreationTimestamp
    private LocalDateTime createdAt;
}