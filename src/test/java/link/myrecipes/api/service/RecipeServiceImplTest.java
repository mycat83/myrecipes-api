package link.myrecipes.api.service;

import link.myrecipes.api.domain.MaterialEntity;
import link.myrecipes.api.domain.RecipeEntity;
import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.dto.request.RecipeMaterialRequest;
import link.myrecipes.api.dto.request.RecipeRequest;
import link.myrecipes.api.dto.request.RecipeStepRequest;
import link.myrecipes.api.dto.request.RecipeTagRequest;
import link.myrecipes.api.dto.view.RecipeView;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.repository.MaterialRepository;
import link.myrecipes.api.repository.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceImplTest {
    private RecipeRequest recipeRequest;

    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Before
    public void setUp() {
        RecipeMaterialRequest recipeMaterialRequest = RecipeMaterialRequest.builder().materialId(1).quantity(5D).build();

        RecipeStepRequest recipeStepRequest = RecipeStepRequest.builder().step(1).content("step1").image("step1.jpg").build();

        RecipeTagRequest recipeTagRequest1 = RecipeTagRequest.builder().tag("tag1").build();
        RecipeTagRequest recipeTagRequest2 = RecipeTagRequest.builder().tag("tag2").build();

        this.recipeRequest = RecipeRequest.builder().title("test1").image("image1.jpg").estimatedTime(30).difficulty(1).build();
        this.recipeRequest.addRecipeMaterial(recipeMaterialRequest);
        this.recipeRequest.addRecipeStep(recipeStepRequest);
        this.recipeRequest.addRecipeTag(recipeTagRequest1);
        this.recipeRequest.addRecipeTag(recipeTagRequest2);

        this.recipe1 = Recipe.builder()
                .title(recipeRequest.getTitle()).image(recipeRequest.getImage())
                .estimatedTime(recipeRequest.getEstimatedTime()).difficulty(recipeRequest.getDifficulty())
                .build();
        this.recipe2 = Recipe.builder().title("test2").image("image2.jpg").estimatedTime(60).difficulty(3).build();
        this.recipe3 = Recipe.builder().title("test3").image("image3.jpg").estimatedTime(90).difficulty(5).build();
    }

    @Test
    public void Should_첫번째_페이지_반환_When_0_페이지_조회() {
        List<Recipe> list = new ArrayList<>();
        list.add(this.recipe1);
        list.add(this.recipe2);
        list.add(this.recipe3);

        Page<RecipeEntity> page = new PageImpl<>(
            list.stream().map(Recipe::toEntity).collect(Collectors.toList()),
            PageRequest.of(0, list.size()), list.size()
        );

        //given
        given(this.recipeRepository.findAll(any(PageRequest.class))).willReturn(page);

        //when
        final List<Recipe> foundList = this.recipeService.readRecipePageSortedByParam(0, 10, "registerDate", false);

        //then
        assertThat(foundList.size(), is(3));
        assertThat(foundList.get(0).getTitle(), is(this.recipe1.getTitle()));
        assertThat(foundList.get(1).getTitle(), is(this.recipe2.getTitle()));
        assertThat(foundList.get(2).getTitle(), is(this.recipe3.getTitle()));
    }

    @Test(expected = NotExistDataException.class)
    public void Should_예외_발생_When_존재하지_않는_ID_조회() {
        //given
        given(this.recipeRepository.findById(1)).willReturn(Optional.empty());

        //when
        this.recipeService.readRecipe(1);
    }

    @Test
    public void Should_정상_저장_확인_When_레시피_저장() {
        MaterialEntity materialEntity = MaterialEntity.builder().name("material1").build();
        Optional<MaterialEntity> materialEntityOptional = Optional.ofNullable(materialEntity);

        RecipeEntity recipeEntity = this.recipeRequest.toEntity();
        for (RecipeMaterialRequest recipeMaterialRequest : this.recipeRequest.getRecipeMaterialRequestList()) {
            recipeEntity.addRecipeMaterial(recipeMaterialRequest.toEntity());
        }
        for (RecipeStepRequest recipeStepRequest : this.recipeRequest.getRecipeStepRequestList()) {
            recipeEntity.addRecipeStep(recipeStepRequest.toEntity());
        }
        for (RecipeTagRequest recipeTagRequest : this.recipeRequest.getRecipeTagRequestList()) {
            recipeEntity.addRecipeTag(recipeTagRequest.toEntity());
        }
        Optional<RecipeEntity> recipeEntityOptional = Optional.ofNullable(recipeEntity);

        //given
        given(this.materialRepository.findById(1)).willReturn(materialEntityOptional);
        given(this.recipeRepository.save(any(RecipeEntity.class))).willReturn(recipe1.toEntity());
        given(this.recipeRepository.findById(1)).willReturn(recipeEntityOptional);

        //when
        final RecipeView recipeView = this.recipeService.readRecipe(1);

        //then
        assertThat(recipeView, instanceOf(RecipeView.class));
        assertThat(recipeView.getTitle(), is(recipeRequest.getTitle()));
        assertThat(recipeView.getImage(), is(recipeRequest.getImage()));
        assertThat(recipeView.getEstimatedTime(), is(recipeRequest.getEstimatedTime()));
        assertThat(recipeView.getDifficulty(), is(recipeRequest.getDifficulty()));

        assertThat(recipeView.getRecipeMaterialViewList().size(), is(recipeRequest.getRecipeMaterialRequestList().size()));
        assertThat(recipeView.getRecipeMaterialViewList().get(0).getQuantity(), is(recipeRequest.getRecipeMaterialRequestList().get(0).getQuantity()));

        assertThat(recipeView.getRecipeStepViewList().size(), is(recipeRequest.getRecipeStepRequestList().size()));
        assertThat(recipeView.getRecipeStepViewList().get(0).getStep(), is(recipeRequest.getRecipeStepRequestList().get(0).getStep()));
        assertThat(recipeView.getRecipeStepViewList().get(0).getContent(), is(recipeRequest.getRecipeStepRequestList().get(0).getContent()));
        assertThat(recipeView.getRecipeStepViewList().get(0).getImage(), is(recipeRequest.getRecipeStepRequestList().get(0).getImage()));

        assertThat(recipeView.getRecipeTagViewList().size(), is(recipeRequest.getRecipeTagRequestList().size()));
        assertThat(recipeView.getRecipeTagViewList().get(0).getTag(), is(recipeRequest.getRecipeTagRequestList().get(0).getTag()));
        assertThat(recipeView.getRecipeTagViewList().get(1).getTag(), is(recipeRequest.getRecipeTagRequestList().get(1).getTag()));
    }

    @Test(expected = NotExistDataException.class)
    public void Should_예외_발생_When_존재하지_않는_재료로_레시피_저장() {
        Optional<MaterialEntity> materialEntityOptional = Optional.empty();

        //given
        given(this.materialRepository.findById(1)).willReturn(materialEntityOptional);

        //when
        this.recipeService.createRecipe(recipeRequest, 10001);
    }

    @Test
    public void Should_업데이트된_항목_반환_When_업데이트_성공() {
        //given
        given(this.recipeRepository.findById(1)).willReturn(Optional.ofNullable(this.recipe1.toEntity()));
        given(this.recipeRepository.save(any(RecipeEntity.class))).willReturn(this.recipe2.toEntity());

        //when
        final Recipe updatedRecipe = this.recipeService.updateRecipe(1, this.recipe2);

        //then
        assertThat(updatedRecipe, not(nullValue()));
        assertThat(updatedRecipe.getTitle(), equalTo(this.recipe2.getTitle()));
        assertThat(updatedRecipe.getImage(), equalTo(this.recipe2.getImage()));
        assertThat(updatedRecipe.getEstimatedTime(), equalTo(this.recipe2.getEstimatedTime()));
        assertThat(updatedRecipe.getDifficulty(), equalTo(this.recipe2.getDifficulty()));
    }

    @Test
    public void Should_Null_반환_When_업데이트_실패() {
        //given
        given(this.recipeRepository.findById(1)).willReturn(Optional.empty());

        //when
        final Recipe updatedRecipe = this.recipeService.updateRecipe(1, this.recipe2);

        //then
        assertThat(updatedRecipe, is(nullValue()));
    }

    @Test
    public void Should_카운트_1_반환_When_1건_조회() {
        //given
        given(this.recipeRepository.count()).willReturn(1L);

        //when
        final long recipeCnt = this.recipeService.readRecipeCnt();

        //then
        assertThat(recipeCnt, is(1L));
    }
}