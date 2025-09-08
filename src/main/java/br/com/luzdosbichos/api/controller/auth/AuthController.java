package br.com.luzdosbichos.api.controller.auth;

import br.com.luzdosbichos.model.account.Account;
import br.com.luzdosbichos.model.account.auth.AuthRequest;
import br.com.luzdosbichos.model.account.auth.RegisterRequest;
import br.com.luzdosbichos.model.account.enums.AccountType;
import br.com.luzdosbichos.security.SecurityConfig;
import br.com.luzdosbichos.service.account.AccountService;
import br.com.luzdosbichos.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final SecurityConfig securityConfig;
    private final AccountService accountService;

    @PostMapping("/login")
    @Operation(summary = "Account login",
            description = "Authenticates a account using email and password, returning a JWT token and account details if successful.")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getEmail(), req.getPassword()
                    )
            );

            Account account = accountService.getAccountByEmail(req.getEmail());

            String token = jwtUtil.generateToken(account.getEmail());

            Map<String, Object> response = Map.of(
                    "token", token,
                    "account", Map.of(
                            "id", account.getId(),
                            "name", account.getName(),
                            "email", account.getEmail()
                    )
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Email ou senha inválidos"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Usuário não encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Um erro inexpectado ocorreu: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Account registration",
            description = "Registers a new account with name, email, phone, address, profile picture and password, returning a JWT token and account details if successful.")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            Account account = new Account();
            account.setName(req.getName());
            account.setEmail(req.getEmail());
            account.setPhone(req.getPhone());
            account.setCpf(req.getCpf());
            account.setGender(req.getGender());
            account.setAccountType(AccountType.USERCOMMON);
            account.setPassword(securityConfig.passwordEncoder().encode(req.getPassword()));

            accountService.save(account);

            String token = jwtUtil.generateToken(account.getEmail());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "account", Map.of(
                            "id", account.getId(),
                            "name", account.getName(),
                            "email", account.getEmail()
                    )
            ));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
