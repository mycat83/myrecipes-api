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
@WebMvcTest(HealthController.class)
public class HealthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void healthTest() throws Exception {
        // given

        // when
        final ResultActions actions = mockMvc.perform(get("/health"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().string("Hello System"));
    }
}