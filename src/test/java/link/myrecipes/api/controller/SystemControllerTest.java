package link.myrecipes.api.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SystemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void When_컨트롤러_호출_Then_정상_응답() throws Exception {

        // When
        final ResultActions actions = this.mockMvc.perform(get("/health"));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

//    @Test
//    public void When_예외_발생_컨트롤러_호출_Then_Advice_예외_처리() throws Exception {
//
//        // When
//        final ResultActions actions = this.mockMvc.perform(get("/exception"));
//
//        // Then
//        actions.andDo(print())
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("status").value(500))
//                .andExpect(jsonPath("message").value(500))
//                .andExpect(content().string(containsString("\"status\":java.lang.NullPointerException")));
//    }
}