package br.com.fabreum.AppProdutos.Service;

import br.com.fabreum.AppProdutos.model.User;
import br.com.fabreum.AppProdutos.model.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    @Test
    @DisplayName("Deve retornar ROLE_CUSTOMER corretamente para clientes")
    void getAuthorities_Customer() {
        User user = new User();
        user.setRole(UserRole.CUSTOMER);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER")));
    }

    @Test
    @DisplayName("Deve retornar ROLE_ADMIN e ROLE_USER para admin")
    void getAuthorities_Admin() {
        User user = new User();
        user.setRole(UserRole.ADMIN);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }
}