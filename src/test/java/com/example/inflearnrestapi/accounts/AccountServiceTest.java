package com.example.inflearnrestapi.accounts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void loadUserByUsername() {
        String email = "test1234@gmail.com";
        String password = "1234";

        accountService.saveAccount(new AccountDto(email, password));

        UserDetails userDetails = accountService.loadUserByUsername(email);

        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
        assertThat(userDetails.getAuthorities())
                .allMatch(role -> role.getAuthority().equals("ROLE_USER"));
    }

    @Test
    @DisplayName("findByUsername - 계정이 존재하지 않을 떄")
    public void loadUserByUsernameWhenAccountDoesntExist() {
        assertThatThrownBy(() -> accountService.loadUserByUsername("test@naver.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}