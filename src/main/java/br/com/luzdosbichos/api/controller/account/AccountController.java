package br.com.luzdosbichos.api.controller.account;

import br.com.luzdosbichos.model.account.Account;
import br.com.luzdosbichos.service.account.AccountService;
import br.com.luzdosbichos.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @Operation(summary = "Get authenticated account details",
            description = "Returns the details of the authenticated account. " +
                    "Access is restricted to the authenticated account only.")
    public ResponseEntity<Account> getAuthenticatedAccount() {
        return ResponseEntity.ok(accountService.getAccountById(JwtUtil.getAuthenticatedAccount().getId()));
    }
}
