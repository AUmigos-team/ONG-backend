package br.com.luzdosbichos.model.account.auth;

import br.com.luzdosbichos.model.account.enums.Gender;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private String password;
    private Gender gender;
}
