package com.example.inflearnrestapi.accounts;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    public Account saveAccount(AccountDto accountDto) {
        // 이미 존재하는 이메일 값이 들어올 때
        if (accountRepository.existsByEmail(accountDto.getEmail())) {
            throw new EmailDuplicationException("중복된 이메일 입니다.");
        }
        Account account = modelMapper.map(accountDto, Account.class);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.addRole(AccountRole.USER);
        return accountRepository.save(account);
    }

    public Authentication login(AccountDto accountDto) {
        UserDetails userDetails = loadUserByUsername(accountDto.getEmail());

        if (!passwordEncoder.matches(accountDto.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("유효하지 않은 패스워드");
        }

        return UsernamePasswordAuthenticationToken
                .authenticated(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        List<SimpleGrantedAuthority> authorities = account.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        return new User(account.getEmail(), account.getPassword(), authorities);
    }
}
