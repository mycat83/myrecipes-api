package io.myrecipes.api.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SystemController.class)
public class SystemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void Should_정상_응답_When_컨트롤러_호출() throws Exception {
        final ResultActions actions = mockMvc.perform(get("/health"));

        actions.andExpect(status().isOk())
                .andExpect(content().string("Hello System"));
    }

    @Test
    public void Shuod_Advice_예외_처리_When_예외_발생_컨트롤러_호출() throws Exception {
        final ResultActions actions = mockMvc.perform(get("/exception"));

        actions.andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"message\":\"java.lang.NullPointerException\"}"));
    }
}