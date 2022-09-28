package com.example.inflearnrestapi.accounts;

import com.example.inflearnrestapi.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @DisplayName("회원 가입")
    void signup() throws Exception {
        AccountDto source = new AccountDto("kimchi@naver.com", "1234");

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(source))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value("kimchi@naver.com"))
                .andExpect(jsonPath("password").value(not("1234")))
                .andExpect(jsonPath("roles").value(hasItem("USER")));
    }

    @Test
    @DisplayName("회원 가입 - 이메일 중복")
    void signupWhenDuplicateEmail() throws Exception {
        // given
        accountService.saveAccount(new AccountDto("kimchi@naver.com", "1234"));

        AccountDto source = new AccountDto("kimchi@naver.com", "1234567");

        // when && then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(source))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists());
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        // given
        accountService.saveAccount(new AccountDto("kimchi@naver.com", "1234"));

        AccountDto source = new AccountDto("kimchi@naver.com", "1234");

        // when && then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(source))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}