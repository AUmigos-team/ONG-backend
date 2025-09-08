package br.com.luzdosbichos.repository.account;

import br.com.luzdosbichos.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("select a from Account a where a.email = :email")
    Optional<Account> findByEmail(String email);

    @Query("select a from Account a where a.cpf = :cpf")
    Optional<Account> findByCpf(String cpf);
}
