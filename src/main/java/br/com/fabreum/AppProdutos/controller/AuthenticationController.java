package br.com.fabreum.AppProdutos.controller;

import br.com.fabreum.AppProdutos.service.dto.AuthenticationDto;
import br.com.fabreum.AppProdutos.service.dto.LoginResponseDto;
import br.com.fabreum.AppProdutos.service.dto.RegisterDto;
import br.com.fabreum.AppProdutos.model.User;
import br.com.fabreum.AppProdutos.repository.UserRepository;
import br.com.fabreum.AppProdutos.service.TokenService;
import br.com.fabreum.AppProdutos.service.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Realizar Login", description = "Autentica o usuário e retorna um Token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@RequestBody @Valid AuthenticationDto data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new ApiResponseDto<>("Login realizado com sucesso!", new LoginResponseDto(token)));
    }

    @Operation(summary = "Registrar Novo Usuário", description = "Cria uma nova conta de usuário (ADMIN, SELLER ou CUSTOMER).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Usuário já existe ou dados inválidos")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<Void>> register(@RequestBody @Valid RegisterDto data){
        if(this.repository.findByLogin(data.login()) != null) {
            return ResponseEntity.badRequest().body(new ApiResponseDto<>("Este login já está em uso."));
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok(new ApiResponseDto<>("Usuário registrado com sucesso!"));
    }

    @Operation(summary = "Dados do Usuário Atual", description = "Retorna informações do usuário logado baseado no Token enviado.")
    @GetMapping("/me")
    public ResponseEntity<String> quemSouEu() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Usuário: " + auth.getName() + " | Permissões: " + auth.getAuthorities());
    }
}