package link.myrecipes.api.controller;

import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IndexControllerTest extends ControllerTest {

    @Test
    public void When_인덱스_조회_Then_API_리스트_리턴() throws Exception {

        // When
        final ResultActions actions = this.mockMvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("_links.units").exists())
                .andExpect(jsonPath("_links.materials").exists())
                .andExpect(jsonPath("_links.recipes").exists())
                .andExpect(jsonPath("_links.members").exists());
    }
}
