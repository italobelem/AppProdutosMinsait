package br.com.fabreum.AppProdutos.model;

public enum UserRole {
    ADMIN("admin"),
    SELLER("seller"),
    CUSTOMER("customer");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}