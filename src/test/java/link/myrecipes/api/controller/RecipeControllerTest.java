package link.myrecipes.api.controller;

import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.dto.request.RecipeRequest;
import link.myrecipes.api.dto.view.RecipeView;
import link.myrecipes.api.service.RecipeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.file.Files;
import java.util.Collections;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {
    private Recipe recipe;
    private RecipeView recipeView;

    @Value("classpath:/json/recipeRequest.json")
    private Resource recipeRequestResource;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeServiceImpl recipeService;

    @Before
    public void setUp() {
        this.recipe = Recipe.builder()
                .id(1)
                .title("레시피")
                .image("recipe.jpg")
                .estimatedTime(30)
                .difficulty(1)
                .build();

        this.recipeView = RecipeView.builder()
                .id(this.recipe.getId())
                .title(this.recipe.getTitle())
                .image(this.recipe.getImage())
                .estimatedTime(this.recipe.getEstimatedTime())
                .difficulty(this.recipe.getDifficulty())
                .build();
    }

    @Test
    public void When_레시피_한_건_조회_When_정상_리턴() throws Exception {
        //given
        given(this.recipeService.readRecipe(eq(this.recipe.getId()))).willReturn(this.recipeView);

        //when
        final ResultActions actions = this.mockMvc.perform(get("/recipes/" + this.recipe.getId()));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("\"id\":" + this.recipe.getId())))
                .andExpect(content().string(containsString("\"title\":\"" + this.recipe.getTitle() + "\"")))
                .andExpect(content().string(containsString("\"image\":\"" + this.recipe.getImage() + "\"")))
                .andExpect(content().string(containsString("\"estimatedTime\":" + this.recipe.getEstimatedTime())))
                .andExpect(content().string(containsString("\"difficulty\":" + this.recipe.getDifficulty())));
    }

    @Test
    public void When_파라미터_없이_레시피_페이지_호출_When_첫페이지_조회() throws Exception {
        //given
        given(this.recipeService.readRecipePageSortedByParam(eq(0), eq(10), eq("registerDate"), eq(false)))
                .willReturn(Collections.singletonList(this.recipe));

        //when
        final ResultActions actions = this.mockMvc.perform(get("/recipes"));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("\"id\":" + this.recipe.getId())))
                .andExpect(content().string(containsString("\"title\":\"" + this.recipe.getTitle() + "\"")))
                .andExpect(content().string(containsString("\"image\":\"" + this.recipe.getImage() + "\"")))
                .andExpect(content().string(containsString("\"estimatedTime\":" + this.recipe.getEstimatedTime())))
                .andExpect(content().string(containsString("\"difficulty\":" + this.recipe.getDifficulty())));
    }

    @Test
    public void When_레시피_저장_When_정상_리턴() throws Exception {
        //given
        String recipeRequestJson = new String(Files.readAllBytes(recipeRequestResource.getFile().toPath()));
        given(this.recipeService.createRecipe(any(RecipeRequest.class), any(Integer.class))).willReturn(this.recipe);

        //when
        final ResultActions actions = this.mockMvc.perform(post("/recipes?userId=10001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(recipeRequestJson));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("\"id\":" + this.recipe.getId())))
                .andExpect(content().string(containsString("\"title\":\"" + this.recipe.getTitle() + "\"")))
                .andExpect(content().string(containsString("\"image\":\"" + this.recipe.getImage() + "\"")))
                .andExpect(content().string(containsString("\"estimatedTime\":" + this.recipe.getEstimatedTime())))
                .andExpect(content().string(containsString("\"difficulty\":" + this.recipe.getDifficulty())));
    }

    @Test
    public void When_레시피_수정_When_정상_리턴() throws Exception {
        //given
        String recipeRequestJson = new String(Files.readAllBytes(recipeRequestResource.getFile().toPath()));
        given(this.recipeService.updateRecipe(eq(this.recipe.getId()), any(RecipeRequest.class), any(Integer.class))).willReturn(this.recipe);

        //when
        final ResultActions actions = this.mockMvc.perform(put("/recipes/" + this.recipe.getId() + "?userId=10001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(recipeRequestJson));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("\"id\":" + this.recipe.getId())))
                .andExpect(content().string(containsString("\"title\":\"" + this.recipe.getTitle() + "\"")))
                .andExpect(content().string(containsString("\"image\":\"" + this.recipe.getImage() + "\"")))
                .andExpect(content().string(containsString("\"estimatedTime\":" + this.recipe.getEstimatedTime())))
                .andExpect(content().string(containsString("\"difficulty\":" + this.recipe.getDifficulty())));
    }

    @Test
    public void When_레시피_삭제_When_정상_리턴() throws Exception {
        //when
        final ResultActions actions = this.mockMvc.perform(delete("/recipes/" + this.recipe.getId()));
    }

    @Test
    public void When_레시피_건_수_조회_When_정상_리턴() throws Exception {
        //given
        given(this.recipeService.readRecipeCnt()).willReturn(10L);

        //when
        final ResultActions actions = this.mockMvc.perform(get("/recipes/cnt"));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("10"));
    }
}