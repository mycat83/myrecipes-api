package link.myrecipes.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import link.myrecipes.api.domain.*;
import link.myrecipes.api.repository.MaterialRepository;
import link.myrecipes.api.repository.RecipeRepository;
import link.myrecipes.api.repository.UnitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Test
    public void When_레시피_한_건_조회_When_정상_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        IntStream.range(0, 30).forEach(i -> saveRecipe(materialEntity, i));
        RecipeEntity recipeEntity = saveRecipe(materialEntity, 30);

        // When
        final ResultActions actions = this.mockMvc.perform(get("/recipes/{id}", recipeEntity.getId()));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").value(recipeEntity.getTitle()))
                .andExpect(jsonPath("image").value(recipeEntity.getImage()))
                .andExpect(jsonPath("estimatedTime").value(recipeEntity.getEstimatedTime()))
                .andExpect(jsonPath("difficulty").value(recipeEntity.getDifficulty()))
                .andExpect(jsonPath("people").value(recipeEntity.getPeople()))
                .andExpect(jsonPath("people").value(recipeEntity.getPeople()));
    }

//    @Test
//    public void When_파라미터_없이_레시피_페이지_호출_When_첫페이지_조회() throws Exception {
//        // Given
//        given(this.recipeService.readRecipePageSortedByParam(eq(0), eq(10), eq("registerDate"), eq(false)))
//                .willReturn(Collections.singletonList(this.recipe));
//
//        // When
//        final ResultActions actions = this.mockMvc.perform(get("/recipes"));
//
//        // Then
//        actions.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(content().string(containsString("\"id\":" + this.recipe.getId())))
//                .andExpect(content().string(containsString("\"title\":\"" + this.recipe.getTitle() + "\"")))
//                .andExpect(content().string(containsString("\"image\":\"" + this.recipe.getImage() + "\"")))
//                .andExpect(content().string(containsString("\"estimatedTime\":" + this.recipe.getEstimatedTime())))
//                .andExpect(content().string(containsString("\"difficulty\":" + this.recipe.getDifficulty())));
//    }

//    @Test
//    public void When_레시피_저장_When_정상_리턴() throws Exception {
//        // Given
//        String recipeRequestJson = new String(Files.readAllBytes(recipeRequestResource.getFile().toPath()));
//        given(this.recipeService.createRecipe(any(RecipeRequest.class), any(Integer.class))).willReturn(this.recipe);
//
//        // When
//        final ResultActions actions = this.mockMvc.perform(post("/recipes?userId=10001")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(recipeRequestJson));
//
//        // Then
//        actions.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(content().string(containsString("\"id\":" + this.recipe.getId())))
//                .andExpect(content().string(containsString("\"title\":\"" + this.recipe.getTitle() + "\"")))
//                .andExpect(content().string(containsString("\"image\":\"" + this.recipe.getImage() + "\"")))
//                .andExpect(content().string(containsString("\"estimatedTime\":" + this.recipe.getEstimatedTime())))
//                .andExpect(content().string(containsString("\"difficulty\":" + this.recipe.getDifficulty())));
//    }
//
//    @Test
//    public void When_레시피_수정_When_정상_리턴() throws Exception {
//        // Given
//        String recipeRequestJson = new String(Files.readAllBytes(recipeRequestResource.getFile().toPath()));
//        given(this.recipeService.updateRecipe(eq(this.recipe.getId()), any(RecipeRequest.class), any(Integer.class))).willReturn(this.recipe);
//
//        // When
//        final ResultActions actions = this.mockMvc.perform(put("/recipes/" + this.recipe.getId() + "?userId=10001")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(recipeRequestJson));
//
//        // Then
//        actions.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(content().string(containsString("\"id\":" + this.recipe.getId())))
//                .andExpect(content().string(containsString("\"title\":\"" + this.recipe.getTitle() + "\"")))
//                .andExpect(content().string(containsString("\"image\":\"" + this.recipe.getImage() + "\"")))
//                .andExpect(content().string(containsString("\"estimatedTime\":" + this.recipe.getEstimatedTime())))
//                .andExpect(content().string(containsString("\"difficulty\":" + this.recipe.getDifficulty())));
//    }
//
//    @Test
//    public void When_레시피_삭제_When_정상_리턴() throws Exception {
//        // When
//        final ResultActions actions = this.mockMvc.perform(delete("/recipes/" + this.recipe.getId()));
//    }
//
//    @Test
//    public void When_레시피_건_수_조회_When_정상_리턴() throws Exception {
//        // Given
//        given(this.recipeService.readRecipeCount()).willReturn(10L);
//
//        // When
//        final ResultActions actions = this.mockMvc.perform(get("/recipes/count"));
//
//        // Then
//        actions.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(content().string("10"));
//    }

    private UnitEntity saveUnit() {

        UnitEntity unitEntity = UnitEntity.builder()
                .name("kg")
                .registerUserId(1001)
                .modifyUserId(1001)
                .build();
        this.unitRepository.save(unitEntity);
        return unitEntity;
    }

    private MaterialEntity saveMaterial(UnitEntity unitEntity) {

        MaterialEntity materialEntity = MaterialEntity.builder()
                .name("재료")
                .registerUserId(1001)
                .modifyUserId(1001)
                .unitEntity(unitEntity)
                .build();
        this.materialRepository.save(materialEntity);
        return materialEntity;
    }

    private RecipeEntity saveRecipe(MaterialEntity materialEntity, int index) {

        RecipeEntity recipeEntity = RecipeEntity.builder()
                .title("레시피" + index)
                .image("recipe.jpg")
                .estimatedTime(30)
                .difficulty(1)
                .people(1)
                .registerUserId(1001)
                .modifyUserId(1001)
                .build();

        RecipeMaterialEntity recipeMaterialEntity = RecipeMaterialEntity.builder()
                .quantity(10D)
                .recipeEntity(recipeEntity)
                .materialEntity(materialEntity)
                .build();

        RecipeStepEntity recipeStepEntity = RecipeStepEntity.builder()
                .step(1)
                .content("1단계")
                .image("step1.jpg")
                .recipeEntity(recipeEntity)
                .build();

        RecipeTagEntity recipeTagEntity = RecipeTagEntity.builder()
                .tag("태그")
                .recipeEntity(recipeEntity)
                .build();

        recipeEntity.addRecipeMaterial(recipeMaterialEntity);
        recipeEntity.addRecipeStep(recipeStepEntity);
        recipeEntity.addRecipeTag(recipeTagEntity);
        this.recipeRepository.save(recipeEntity);
        return recipeEntity;
    }
}