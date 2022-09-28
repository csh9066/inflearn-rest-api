package com.example.inflearnrestapi.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@Entity
public class Account {

    @Id @GeneratedValue
    private Integer id;

    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles = new HashSet<>();

    public void addRole(AccountRole role) {
        roles.add(role);
    }
}
