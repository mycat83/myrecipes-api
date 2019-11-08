package link.myrecipes.api.controller;

import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.service.RecipeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeServiceImpl recipeService;

    @Test
    public void Should_첫페이지_조회_When_쿼리스트링_없이_레시피_페이지_호출() throws Exception {
        Recipe recipe = Recipe.builder().title("test1").image("image1.jpg").estimatedTime(30).difficulty(1).build();

        //given
        given(this.recipeService.readRecipePageSortedByParam(eq(0), eq(10), eq("registerDate"), eq(false))).willReturn(Collections.singletonList(recipe));

        //when
        final ResultActions actions = this.mockMvc.perform(get("/recipes"));

        //then
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

}