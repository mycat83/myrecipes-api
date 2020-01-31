package link.myrecipes.api.service;

import link.myrecipes.api.domain.*;
import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.dto.RecipeCount;
import link.myrecipes.api.dto.request.RecipeMaterialRequest;
import link.myrecipes.api.dto.request.RecipeRequest;
import link.myrecipes.api.dto.request.RecipeStepRequest;
import link.myrecipes.api.dto.request.RecipeTagRequest;
import link.myrecipes.api.dto.view.RecipeMaterialView;
import link.myrecipes.api.dto.view.RecipeStepView;
import link.myrecipes.api.dto.view.RecipeTagView;
import link.myrecipes.api.dto.view.RecipeView;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceImplTest {

    private RecipeRequest recipeRequest1;
    private RecipeRequest recipeRequest2;

    private Recipe recipe1;
    private Recipe recipe2;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private RecipeMaterialRepository recipeMaterialRepository;

    @Mock
    private RecipeStepRepository recipeStepRepository;

    @Mock
    private RecipeTagRepository recipeTagRepository;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void setUp() {

        RecipeMaterialRequest recipeMaterialRequest1 = RecipeMaterialRequest.builder().materialId(1).quantity(5D).build();
        RecipeMaterialRequest recipeMaterialRequest2 = RecipeMaterialRequest.builder().materialId(2).quantity(10D).build();

        RecipeStepRequest recipeStepRequest1 = RecipeStepRequest.builder().step(1).content("step1").image("step1.jpg").build();
        RecipeStepRequest recipeStepRequest2 = RecipeStepRequest.builder().step(1).content("step1-1").image("step1-1.jpg").build();

        RecipeTagRequest recipeTagRequest1 = RecipeTagRequest.builder().tag("tag1").build();
        RecipeTagRequest recipeTagRequest2 = RecipeTagRequest.builder().tag("tag2").build();

        this.recipeRequest1 = RecipeRequest.builder().title("test1").image("image1.jpg").estimatedTime(30).difficulty(1).build();
        this.recipeRequest1.addRecipeMaterial(recipeMaterialRequest1);
        this.recipeRequest1.addRecipeStep(recipeStepRequest1);
        this.recipeRequest1.addRecipeTag(recipeTagRequest1);

        this.recipeRequest2 = RecipeRequest.builder().title("test2").image("image2.jpg").estimatedTime(60).difficulty(3).build();
        this.recipeRequest2.addRecipeMaterial(recipeMaterialRequest2);
        this.recipeRequest2.addRecipeStep(recipeStepRequest2);
        this.recipeRequest2.addRecipeTag(recipeTagRequest2);

        this.recipe1 = Recipe.builder()
                .title(recipeRequest1.getTitle()).image(recipeRequest1.getImage())
                .estimatedTime(recipeRequest1.getEstimatedTime()).difficulty(recipeRequest1.getDifficulty())
                .build();
        this.recipe2 = Recipe.builder()
                .title(recipeRequest2.getTitle()).image(recipeRequest2.getImage())
                .estimatedTime(recipeRequest2.getEstimatedTime()).difficulty(recipeRequest2.getDifficulty())
                .build();
    }

    @Test
    public void When_0_페이지_조회_Then_첫번째_페이지_반환() {

        // Given
        RecipeEntity recipeEntity = makeRecipeEntity(this.recipeRequest1);
        Page<RecipeEntity> page = new PageImpl<>(Collections.singletonList(recipeEntity));
        given(this.recipeRepository.findAll(any(PageRequest.class))).willReturn(page);
        given(this.modelMapper.map(any(RecipeEntity.class), eq(Recipe.class))).willReturn(this.recipe1);

        // When
        final Page<Recipe> foundList = this.recipeService.readRecipeList(PageRequest.of(0, 10));

        // Then
        assertThat(foundList.getTotalElements(), is(1L));
        assertThat(foundList.getContent().get(0).getTitle(), is(this.recipe1.getTitle()));
        assertThat(foundList.getContent().get(0).getImage(), is(this.recipe1.getImage()));
        assertThat(foundList.getContent().get(0).getEstimatedTime(), is(this.recipe1.getEstimatedTime()));
        assertThat(foundList.getContent().get(0).getDifficulty(), is(this.recipe1.getDifficulty()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하지_않는_ID_조회_Then_예외_발생() {

        // Given
        given(this.recipeRepository.findById(1)).willReturn(Optional.empty());

        // When
        this.recipeService.readRecipe(1);
    }

    @Test
    public void When_레시피_저장_Then_정상_저장_확인() {

        // Given
        RecipeEntity recipeEntity = makeRecipeEntity(this.recipeRequest1);
        Optional<RecipeEntity> recipeEntityOptional = Optional.ofNullable(recipeEntity);

        RecipeView recipeView = makeRecipeView(this.recipeRequest1);

        given(this.recipeRepository.findById(1)).willReturn(recipeEntityOptional);
        given(this.modelMapper.map(any(RecipeEntity.class), eq(RecipeView.class))).willReturn(recipeView);

        // When
        final RecipeView readRecipeView = this.recipeService.readRecipe(1);

        // Then
        assertThat(readRecipeView, instanceOf(RecipeView.class));
        assertThat(readRecipeView.getTitle(), is(recipeRequest1.getTitle()));
        assertThat(readRecipeView.getImage(), is(recipeRequest1.getImage()));
        assertThat(readRecipeView.getEstimatedTime(), is(recipeRequest1.getEstimatedTime()));
        assertThat(readRecipeView.getDifficulty(), is(recipeRequest1.getDifficulty()));

        assertThat(readRecipeView.getRecipeMaterialList().size(), is(recipeRequest1.getRecipeMaterialRequestList().size()));
        assertThat(readRecipeView.getRecipeMaterialList().get(0).getQuantity(), is(recipeRequest1.getRecipeMaterialRequestList().get(0).getQuantity()));

        assertThat(readRecipeView.getRecipeStepList().size(), is(recipeRequest1.getRecipeStepRequestList().size()));
        assertThat(readRecipeView.getRecipeStepList().get(0).getStep(), is(recipeRequest1.getRecipeStepRequestList().get(0).getStep()));
        assertThat(readRecipeView.getRecipeStepList().get(0).getContent(), is(recipeRequest1.getRecipeStepRequestList().get(0).getContent()));
        assertThat(readRecipeView.getRecipeStepList().get(0).getImage(), is(recipeRequest1.getRecipeStepRequestList().get(0).getImage()));

        assertThat(readRecipeView.getRecipeTagList().size(), is(recipeRequest1.getRecipeTagRequestList().size()));
        assertThat(readRecipeView.getRecipeTagList().get(0).getTag(), is(recipeRequest1.getRecipeTagRequestList().get(0).getTag()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하지_않는_재료로_레시피_저장_Then_예외_발생() {

        // Given
        Optional<MaterialEntity> materialEntityOptional = Optional.empty();
        RecipeEntity recipeEntity = makeRecipeEntity(this.recipeRequest1);

        given(this.materialRepository.findById(1)).willReturn(materialEntityOptional);
        given(this.modelMapper.map(any(RecipeRequest.class), eq(RecipeEntity.class))).willReturn(recipeEntity);

        // When
        this.recipeService.createRecipe(recipeRequest1, 10001);
    }

    @Test
    public void When_업데이트_성공_Then_업데이트된_항목_반환() {

        // Given
        MaterialEntity materialEntity = MaterialEntity.builder()
                .name("material")
                .build();
        RecipeMaterialEntity recipeMaterialEntity1 = RecipeMaterialEntity.builder()
                .quantity(10D)
                .materialEntity(materialEntity)
                .build();
        RecipeStepEntity recipeStepEntity1 = RecipeStepEntity.builder()
                .step(1)
                .content("step1")
                .image("step1.jpg")
                .build();
        RecipeTagEntity recipeTagEntity1 = RecipeTagEntity.builder()
                .tag("tag1")
                .build();
        RecipeMaterialEntity recipeMaterialEntity2 = RecipeMaterialEntity.builder()
                .quantity(20D)
                .materialEntity(materialEntity)
                .build();
        RecipeStepEntity recipeStepEntity2 = RecipeStepEntity.builder()
                .step(1)
                .content("step2")
                .image("step2.jpg")
                .build();
        RecipeTagEntity recipeTagEntity2 = RecipeTagEntity.builder()
                .tag("tag2")
                .build();

        RecipeEntity recipeEntity1 = makeRecipeEntity(this.recipe1);
        recipeEntity1.addRecipeMaterial(recipeMaterialEntity1);
        recipeEntity1.addRecipeStep(recipeStepEntity1);
        recipeEntity1.addRecipeTag(recipeTagEntity1);
        recipeEntity1.setId(1);

        RecipeEntity recipeEntity2 = makeRecipeEntity(this.recipe2);
        recipeEntity2.addRecipeMaterial(recipeMaterialEntity2);
        recipeEntity2.addRecipeStep(recipeStepEntity2);
        recipeEntity2.addRecipeTag(recipeTagEntity2);
        recipeEntity2.setId(1);

        Recipe recipe = makeRecipe(recipeEntity2);

        given(this.recipeRepository.findById(1)).willReturn(Optional.of(recipeEntity1));
        given(this.recipeRepository.save(any(RecipeEntity.class))).willReturn(recipeEntity2);
        given(this.materialRepository.findById(any(Integer.class))).willReturn(Optional.ofNullable(materialEntity));
        given(this.modelMapper.map(any(RecipeRequest.class), eq(RecipeEntity.class))).willReturn(recipeEntity2);
        given(this.modelMapper.map(any(RecipeStepRequest.class), eq(RecipeStepEntity.class)))
                .willReturn(recipeEntity2.getRecipeStepEntityList().get(0));
        given(this.modelMapper.map(any(RecipeTagRequest.class), eq(RecipeTagEntity.class)))
                .willReturn(recipeEntity2.getRecipeTagEntityList().get(0));
        given(this.modelMapper.map(any(RecipeEntity.class), eq(Recipe.class))).willReturn(recipe);

        // When
        final Recipe updatedRecipe = this.recipeService.updateRecipe(recipeEntity2.getId(), this.recipeRequest2, 10002);

        // Then
        assertThat(updatedRecipe, not(nullValue()));
        assertThat(updatedRecipe.getTitle(), equalTo(recipeEntity2.getTitle()));
        assertThat(updatedRecipe.getImage(), equalTo(recipeEntity2.getImage()));
        assertThat(updatedRecipe.getEstimatedTime(), equalTo(recipeEntity2.getEstimatedTime()));
        assertThat(updatedRecipe.getDifficulty(), equalTo(recipeEntity2.getDifficulty()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하지_않는_레시피_수정_Then_예외_발생() {

        // When
        this.recipeService.updateRecipe(2, this.recipeRequest2, 10002);
    }

    @Test(expected = NotExistDataException.class)
    public void When_재료_미등록_상태에서_레시피_수정_Then_예외_발생() {

        // Given
        RecipeEntity recipeEntity1 = makeRecipeEntity(this.recipe1);
        RecipeEntity recipeEntity2 = makeRecipeEntity(this.recipe2);

        recipeEntity2.setId(1);
        given(this.recipeRepository.findById(1)).willReturn(Optional.ofNullable(recipeEntity1));
        given(this.modelMapper.map(any(RecipeRequest.class), eq(RecipeEntity.class))).willReturn(recipeEntity2);

        // When
        this.recipeService.updateRecipe(recipeEntity2.getId(), this.recipeRequest2, 10002);
    }

    @Test
    public void When_레시피_삭제_Then_이상_없음() {

        // When
        this.recipeService.deleteRecipe(1);

        // Then
        assertThat(true, is(true));
    }

    @Test
    public void When_1건_조회_Then_카운트_1_반환() {

        // Given
        given(this.recipeRepository.count()).willReturn(1L);

        // When
        final RecipeCount recipeCnt = this.recipeService.readRecipeCount();

        // Then
        assertThat(recipeCnt.getCount(), is(1L));
    }

    private Recipe makeRecipe(RecipeEntity recipeEntity) {
        return Recipe.builder()
                .id(recipeEntity.getId())
                .title(recipeEntity.getTitle())
                .image(recipeEntity.getImage())
                .estimatedTime(recipeEntity.getEstimatedTime())
                .difficulty(recipeEntity.getDifficulty())
                .build();
    }

    private RecipeEntity makeRecipeEntity(Recipe recipe) {
        return RecipeEntity.builder()
                .title(recipe.getTitle())
                .image(recipe.getImage())
                .estimatedTime(recipe.getEstimatedTime())
                .difficulty(recipe.getEstimatedTime())
                .build();
    }

    private RecipeView makeRecipeView(RecipeRequest recipeRequest) {
        RecipeView recipeView = RecipeView.builder()
                .title(recipeRequest.getTitle())
                .image(recipeRequest.getImage())
                .estimatedTime(recipeRequest.getEstimatedTime())
                .difficulty(recipeRequest.getDifficulty())
                .people(recipeRequest.getPeople())
                .build();
        for (int i = 0; i < recipeRequest.getRecipeMaterialRequestList().size(); i++) {
            recipeView.addRecipeMaterial(RecipeMaterialView.builder()
                    .materialId(recipeRequest.getRecipeMaterialRequestList().get(i).getMaterialId())
                    .quantity(recipeRequest.getRecipeMaterialRequestList().get(i).getQuantity())
                    .build());
        }
        for (int i = 0; i < recipeRequest.getRecipeStepRequestList().size(); i++) {
            recipeView.addRecipeStep(RecipeStepView.builder()
                    .step(recipeRequest.getRecipeStepRequestList().get(0).getStep())
                    .image(recipeRequest.getRecipeStepRequestList().get(0).getImage())
                    .content(recipeRequest.getRecipeStepRequestList().get(0).getContent())
                    .build());
        }
        for (int i = 0; i < recipeRequest.getRecipeTagRequestList().size(); i++) {
            recipeView.addRecipeTag(RecipeTagView.builder()
                    .tag(recipeRequest.getRecipeTagRequestList().get(0).getTag())
                    .build());
        }
        return recipeView;
    }

    private RecipeEntity makeRecipeEntity(RecipeRequest recipeRequest) {
        RecipeEntity recipeEntity = RecipeEntity.builder()
                .title(recipeRequest.getTitle())
                .image(recipeRequest.getImage())
                .estimatedTime(recipeRequest.getEstimatedTime())
                .difficulty(recipeRequest.getDifficulty())
                .people(recipeRequest.getPeople())
                .build();
        for (int i = 0; i < recipeRequest.getRecipeMaterialRequestList().size(); i++) {
            recipeEntity.addRecipeMaterial(RecipeMaterialEntity.builder()
                    .quantity(recipeRequest.getRecipeMaterialRequestList().get(i).getQuantity())
                    .build());
        }
        for (int i = 0; i < recipeRequest.getRecipeStepRequestList().size(); i++) {
            recipeEntity.addRecipeStep(RecipeStepEntity.builder()
                    .step(recipeRequest.getRecipeStepRequestList().get(0).getStep())
                    .image(recipeRequest.getRecipeStepRequestList().get(0).getImage())
                    .content(recipeRequest.getRecipeStepRequestList().get(0).getContent())
                    .build());
        }
        for (int i = 0; i < recipeRequest.getRecipeTagRequestList().size(); i++) {
            recipeEntity.addRecipeTag(RecipeTagEntity.builder()
                    .tag(recipeRequest.getRecipeTagRequestList().get(0).getTag())
                    .build());
        }
        return recipeEntity;
    }
}