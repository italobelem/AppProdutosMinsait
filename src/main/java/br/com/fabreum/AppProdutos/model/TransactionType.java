package br.com.fabreum.AppProdutos.model;

public enum TransactionType {
    PURCHASE("Compra de Fornecedor"),
    SALE("Venda ao Cliente"),
    ADJUSTMENT("Ajuste de Inventário"),
    RETURN("Devolução");

    private String description;

    TransactionType(String description) {
        this.description = description;
    }
}