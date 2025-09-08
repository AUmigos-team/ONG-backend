package br.com.luzdosbichos.service.account;

import br.com.luzdosbichos.model.account.Account;
import br.com.luzdosbichos.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElse(null);
    }

    public Account getAccountById(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public void save(Account account) {
        Optional<Account> optional = accountRepository.findByEmail(account.getEmail());

        if (optional.isPresent()) {
            if (!optional.get().getId().equals(account.getId())) throw new RuntimeException("Email já está em uso");
        }

        optional = accountRepository.findByCpf(account.getCpf());

        if (optional.isPresent()) {
            if (!optional.get().getId().equals(account.getId())) throw new RuntimeException("CPF já está em uso");
        }

        accountRepository.save(account);
    }
}
