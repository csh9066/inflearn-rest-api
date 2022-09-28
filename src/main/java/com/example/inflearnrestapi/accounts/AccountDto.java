package com.example.inflearnrestapi.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    @Email
    private String email;

    @NotBlank
    private String password;
}
