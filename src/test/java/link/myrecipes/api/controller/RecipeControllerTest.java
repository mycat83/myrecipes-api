package link.myrecipes.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import link.myrecipes.api.domain.*;
import link.myrecipes.api.dto.request.RecipeMaterialRequest;
import link.myrecipes.api.dto.request.RecipeRequest;
import link.myrecipes.api.dto.request.RecipeStepRequest;
import link.myrecipes.api.dto.request.RecipeTagRequest;
import link.myrecipes.api.repository.MaterialRepository;
import link.myrecipes.api.repository.PopularRecipeRepository;
import link.myrecipes.api.repository.RecipeRepository;
import link.myrecipes.api.repository.UnitRepository;
import org.junit.After;
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

import java.util.Collections;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Autowired
    private PopularRecipeRepository popularRecipeRepository;

    @After
    public void tearDown() {
        this.recipeRepository.deleteAll();
    }

    @Test
    public void When_레시피_한_건_조회_Then_정상_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        RecipeEntity recipeEntity = saveRecipe(materialEntity, 1);

        // When
        final ResultActions actions = this.mockMvc.perform(get("/recipes/{id}", recipeEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaTypes.HAL_JSON)
                );

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

    @Test
    public void When_레시피_페이지_호출_Then_첫페이지_조회() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        IntStream.range(1, 30).forEach(i -> saveRecipe(materialEntity, i));
        RecipeEntity recipeEntity = saveRecipe(materialEntity, 30);

        // When
        final ResultActions actions = this.mockMvc.perform(get("/recipes")
                .param("page", "1")
                .param("size", "10")
                .param("sortField", "title")
                .param("isDescending", "true")
                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaTypes.HAL_JSON)
                );

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").value(recipeEntity.getTitle()))
                .andExpect(jsonPath("$[0].image").value(recipeEntity.getImage()))
                .andExpect(jsonPath("$[0].estimatedTime").value(recipeEntity.getEstimatedTime()))
                .andExpect(jsonPath("$[0].difficulty").value(recipeEntity.getDifficulty()))
                .andExpect(jsonPath("$[0].recipeTagList[0].tag").value(recipeEntity.getRecipeTagEntityList().get(0).getTag()));
    }

    @Test
    public void When_레시피_저장_Then_정상_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);

        RecipeRequest recipeRequest = RecipeRequest.builder()
                .title("레시피")
                .image("recipe.jpg")
                .estimatedTime(30)
                .difficulty(1)
                .people(1)
                .build();

        RecipeMaterialRequest recipeMaterialRequest = RecipeMaterialRequest.builder()
                .materialId(materialEntity.getId())
                .quantity(10D)
                .build();

        RecipeStepRequest recipeStepRequest = RecipeStepRequest.builder()
                .step(1)
                .content("1단계")
                .image("step1.jpg")
                .build();

        RecipeTagRequest recipeTagRequest = RecipeTagRequest.builder()
                .tag("태그")
                .build();

        recipeRequest.addRecipeMaterial(recipeMaterialRequest);
        recipeRequest.addRecipeStep(recipeStepRequest);
        recipeRequest.addRecipeTag(recipeTagRequest);

        // When
        final ResultActions actions = this.mockMvc.perform(post("/recipes")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(recipeRequest)));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(status().isCreated())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").value(recipeRequest.getTitle()))
                .andExpect(jsonPath("image").value(recipeRequest.getImage()))
                .andExpect(jsonPath("estimatedTime").value(recipeRequest.getEstimatedTime()))
                .andExpect(jsonPath("difficulty").value(recipeRequest.getDifficulty()))
                .andExpect(jsonPath("recipeTagList[0].tag").value(recipeTagRequest.getTag()));
    }

    @Test
    public void When_레시피_수정_Then_정상_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        RecipeEntity recipeEntity = saveRecipe(materialEntity, 1);

        RecipeRequest recipeRequest = RecipeRequest.builder()
                .title("레시피02")
                .image("recipe02.jpg")
                .estimatedTime(60)
                .difficulty(2)
                .people(2)
                .build();

        RecipeMaterialRequest recipeMaterialRequest = RecipeMaterialRequest.builder()
                .materialId(materialEntity.getId())
                .quantity(20D)
                .build();

        RecipeStepRequest recipeStepRequest = RecipeStepRequest.builder()
                .step(2)
                .content("2단계")
                .image("step2.jpg")
                .build();

        RecipeTagRequest recipeTagRequest = RecipeTagRequest.builder()
                .tag("태그2")
                .build();

        recipeRequest.addRecipeMaterial(recipeMaterialRequest);
        recipeRequest.addRecipeStep(recipeStepRequest);
        recipeRequest.addRecipeTag(recipeTagRequest);

        // When
        final ResultActions actions = this.mockMvc.perform(put("/recipes/{id}", recipeEntity.getId())
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(recipeRequest)));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(status().isCreated())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").value(recipeRequest.getTitle()))
                .andExpect(jsonPath("image").value(recipeRequest.getImage()))
                .andExpect(jsonPath("estimatedTime").value(recipeRequest.getEstimatedTime()))
                .andExpect(jsonPath("difficulty").value(recipeRequest.getDifficulty()))
                .andExpect(jsonPath("recipeTagList[0].tag").value(recipeTagRequest.getTag()));
    }

    @Test
    public void When_레시피_삭제_Then_정상_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        RecipeEntity recipeEntity = saveRecipe(materialEntity, 1);

        // When
        final ResultActions actions = this.mockMvc.perform(delete("/recipes/{id}", recipeEntity.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void When_레시피_건_수_조회_Then_정상_리턴() throws Exception {

        // Given
        int count = 10;
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        IntStream.range(0, count).forEach(i -> saveRecipe(materialEntity, i));

        // When
        final ResultActions actions = this.mockMvc.perform(get("/recipes/count"));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(count)));
    }

    @Test
    public void When_레시피_조회_Then_조회수_증가() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        RecipeEntity recipeEntity = saveRecipe(materialEntity, 1);

        // When
        final ResultActions actions = this.mockMvc.perform(put("/recipes/{id}/readCount", recipeEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaTypes.HAL_JSON)
        );

        // Then
        actions.andDo(print())
                .andExpect(status().isOk());

        Optional<RecipeEntity> recipeEntityOptional = this.recipeRepository.findById(recipeEntity.getId());
        if (recipeEntityOptional.isEmpty()) {
            fail("Finding recipe failed.");
        }
        assertThat(recipeEntityOptional.get().getPeople(), is(1));
    }

    @Test
    public void When_인기_레시피_조회_Then_정상_조회() throws Exception {

        // Given
        PopularRecipeDocument popularRecipeDocument = PopularRecipeDocument.builder()
                .id(1)
                .title("레시피")
                .image("recipe.jpg")
                .estimatedTime(30)
                .difficulty(1)
                .people(1)
                .readCount(1)
                .recipeTagList(Collections.singletonList("태그"))
                .build();
        this.popularRecipeRepository.save(popularRecipeDocument);

        // When
        final ResultActions actions = this.mockMvc.perform(get("/popularRecipes")
                        .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaTypes.HAL_JSON)
        );

        // Then -> .andExpect(jsonPath("title").value(recipeRequest.getTitle())) 형태로 변경
        actions.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$[0].id").value(popularRecipeDocument.getId()))
                .andExpect(jsonPath("$[0].title").value(popularRecipeDocument.getTitle()))
                .andExpect(jsonPath("$[0].image").value(popularRecipeDocument.getImage()))
                .andExpect(jsonPath("$[0].estimatedTime").value(popularRecipeDocument.getEstimatedTime()))
                .andExpect(jsonPath("$[0].difficulty").value(popularRecipeDocument.getDifficulty()))
                .andExpect(jsonPath("$[0].recipeTagList[0].tag").value(popularRecipeDocument.getRecipeTagList().get(0)));
    }

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
                .title("레시피" + String.format("%02d", index))
                .image("recipe"  + String.format("%02d", index) + ".jpg")
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