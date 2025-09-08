package br.com.luzdosbichos.model.account.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}