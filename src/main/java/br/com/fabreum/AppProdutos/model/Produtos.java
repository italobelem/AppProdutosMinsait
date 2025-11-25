package br.com.fabreum.AppProdutos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_produtos")
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do produto (Gerado automaticamente)", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Código de barras", example = "IPHONE-15-PRO")
    @Column(name = "codigo_barras")
    private String codigoBarras;

    @Schema(description = "Nome do produto", example = "iPhone 15 Pro Max")
    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;

    @Schema(description = "Preço de venda", example = "8500.00")
    @Column(nullable = false)
    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser maior que zero")
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}