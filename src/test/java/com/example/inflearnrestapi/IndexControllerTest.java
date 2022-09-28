package com.example.inflearnrestapi;

import com.example.inflearnrestapi.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IndexControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("/api 요청")
    void indexPageTest() throws Exception {
        mockMvc.perform(get("/api"))
                .andExpect(status().isOk());
    }

}