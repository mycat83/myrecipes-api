package link.myrecipes.api.service;

import link.myrecipes.api.domain.*;
import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.dto.request.RecipeMaterialRequest;
import link.myrecipes.api.dto.request.RecipeRequest;
import link.myrecipes.api.dto.request.RecipeStepRequest;
import link.myrecipes.api.dto.request.RecipeTagRequest;
import link.myrecipes.api.dto.view.RecipeView;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.repository.*;
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
    private RecipeRequest recipeRequest1;
    private RecipeRequest recipeRequest2;

    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMaterialRepository recipeMaterialRepository;

    @Mock
    private RecipeStepRepository recipeStepRepository;

    @Mock
    private RecipeTagRepository recipeTagRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Before
    public void setUp() {
        RecipeMaterialRequest recipeMaterialRequest1 = RecipeMaterialRequest.builder().materialId(1).quantity(5D).build();
        RecipeMaterialRequest recipeMaterialRequest2 = RecipeMaterialRequest.builder().materialId(2).quantity(10D).build();

        RecipeStepRequest recipeStepRequest1 = RecipeStepRequest.builder().step(1).content("step1").image("step1.jpg").build();
        RecipeStepRequest recipeStepRequest2 = RecipeStepRequest.builder().step(1).content("step1-1").image("step1-1.jpg").build();

        RecipeTagRequest recipeTagRequest1 = RecipeTagRequest.builder().tag("tag1").build();
        RecipeTagRequest recipeTagRequest2 = RecipeTagRequest.builder().tag("tag2").build();
        RecipeTagRequest recipeTagRequest3 = RecipeTagRequest.builder().tag("tag3").build();

        this.recipeRequest1 = RecipeRequest.builder().title("test1").image("image1.jpg").estimatedTime(30).difficulty(1).build();
        this.recipeRequest1.addRecipeMaterial(recipeMaterialRequest1);
        this.recipeRequest1.addRecipeStep(recipeStepRequest1);
        this.recipeRequest1.addRecipeTag(recipeTagRequest1);
        this.recipeRequest1.addRecipeTag(recipeTagRequest2);

        this.recipeRequest2 = RecipeRequest.builder().title("test2").image("image2.jpg").estimatedTime(60).difficulty(3).build();
        this.recipeRequest2.addRecipeMaterial(recipeMaterialRequest2);
        this.recipeRequest2.addRecipeStep(recipeStepRequest2);
        this.recipeRequest2.addRecipeTag(recipeTagRequest2);
        this.recipeRequest2.addRecipeTag(recipeTagRequest3);

        this.recipe1 = Recipe.builder()
                .title(recipeRequest1.getTitle()).image(recipeRequest1.getImage())
                .estimatedTime(recipeRequest1.getEstimatedTime()).difficulty(recipeRequest1.getDifficulty())
                .build();
        this.recipe2 = Recipe.builder()
                .title(recipeRequest2.getTitle()).image(recipeRequest2.getImage())
                .estimatedTime(recipeRequest2.getEstimatedTime()).difficulty(recipeRequest2.getDifficulty())
                .build();
        this.recipe3 = Recipe.builder().title("test3").image("image3.jpg").estimatedTime(90).difficulty(5).build();
    }

    @Test
    public void When_0_페이지_조회_Then_첫번째_페이지_반환() {
        //given
        List<Recipe> list = new ArrayList<>();
        list.add(this.recipe1);
        list.add(this.recipe2);
        list.add(this.recipe3);

        Page<RecipeEntity> page = new PageImpl<>(
                list.stream().map(Recipe::toEntity).collect(Collectors.toList()),
                PageRequest.of(0, list.size()), list.size()
        );

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
    public void When_존재하지_않는_ID_조회_Then_예외_발생() {
        //given
        given(this.recipeRepository.findById(1)).willReturn(Optional.empty());

        //when
        this.recipeService.readRecipe(1);
    }

    @Test
    public void When_레시피_저장_Then_정상_저장_확인() {
        //given
        MaterialEntity materialEntity = MaterialEntity.builder().name("material1").build();
        Optional<MaterialEntity> materialEntityOptional = Optional.ofNullable(materialEntity);

        RecipeEntity recipeEntity = this.recipeRequest1.toEntity();
        for (RecipeMaterialRequest recipeMaterialRequest : this.recipeRequest1.getRecipeMaterialRequestList()) {
            recipeEntity.addRecipeMaterial(recipeMaterialRequest.toEntity());
        }
        for (RecipeStepRequest recipeStepRequest : this.recipeRequest1.getRecipeStepRequestList()) {
            recipeEntity.addRecipeStep(recipeStepRequest.toEntity());
        }
        for (RecipeTagRequest recipeTagRequest : this.recipeRequest1.getRecipeTagRequestList()) {
            recipeEntity.addRecipeTag(recipeTagRequest.toEntity());
        }
        Optional<RecipeEntity> recipeEntityOptional = Optional.ofNullable(recipeEntity);

        given(this.recipeRepository.findById(1)).willReturn(recipeEntityOptional);

        //when
        final RecipeView recipeView = this.recipeService.readRecipe(1);

        //then
        assertThat(recipeView, instanceOf(RecipeView.class));
        assertThat(recipeView.getTitle(), is(recipeRequest1.getTitle()));
        assertThat(recipeView.getImage(), is(recipeRequest1.getImage()));
        assertThat(recipeView.getEstimatedTime(), is(recipeRequest1.getEstimatedTime()));
        assertThat(recipeView.getDifficulty(), is(recipeRequest1.getDifficulty()));

        assertThat(recipeView.getRecipeMaterialViewList().size(), is(recipeRequest1.getRecipeMaterialRequestList().size()));
        assertThat(recipeView.getRecipeMaterialViewList().get(0).getQuantity(), is(recipeRequest1.getRecipeMaterialRequestList().get(0).getQuantity()));

        assertThat(recipeView.getRecipeStepViewList().size(), is(recipeRequest1.getRecipeStepRequestList().size()));
        assertThat(recipeView.getRecipeStepViewList().get(0).getStep(), is(recipeRequest1.getRecipeStepRequestList().get(0).getStep()));
        assertThat(recipeView.getRecipeStepViewList().get(0).getContent(), is(recipeRequest1.getRecipeStepRequestList().get(0).getContent()));
        assertThat(recipeView.getRecipeStepViewList().get(0).getImage(), is(recipeRequest1.getRecipeStepRequestList().get(0).getImage()));

        assertThat(recipeView.getRecipeTagViewList().size(), is(recipeRequest1.getRecipeTagRequestList().size()));
        assertThat(recipeView.getRecipeTagViewList().get(0).getTag(), is(recipeRequest1.getRecipeTagRequestList().get(0).getTag()));
        assertThat(recipeView.getRecipeTagViewList().get(1).getTag(), is(recipeRequest1.getRecipeTagRequestList().get(1).getTag()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하지_않는_재료로_레시피_저장_Then_예외_발생() {
        //given
        Optional<MaterialEntity> materialEntityOptional = Optional.empty();
        given(this.materialRepository.findById(1)).willReturn(materialEntityOptional);

        //when
        this.recipeService.createRecipe(recipeRequest1, 10001);
    }

    @Test
    public void When_업데이트_성공_Then_업데이트된_항목_반환() {
        //given
        MaterialEntity materialEntity = MaterialEntity.builder().name("material").build();
        RecipeMaterialEntity recipeMaterialEntity = RecipeMaterialEntity.builder().quantity(5D).materialEntity(materialEntity).build();
        RecipeStepEntity recipeStepEntity = RecipeStepEntity.builder().step(1).content("step1").image("step1.jpg").build();
        RecipeTagEntity recipeTagEntity = RecipeTagEntity.builder().tag("tag1").build();

        RecipeEntity recipeEntity1 = this.recipe1.toEntity();
        recipeEntity1.addRecipeMaterial(recipeMaterialEntity);
        recipeEntity1.addRecipeStep(recipeStepEntity);
        recipeEntity1.addRecipeTag(recipeTagEntity);

        RecipeEntity recipeEntity2 = this.recipe2.toEntity();
        recipeEntity2.setId(1);

        given(this.recipeRepository.findById(1)).willReturn(Optional.of(recipeEntity1));
        given(this.recipeRepository.save(any(RecipeEntity.class))).willReturn(recipeEntity2);
        given(this.materialRepository.findById(any(Integer.class))).willReturn(Optional.ofNullable(materialEntity));

        //when
        final Recipe updatedRecipe = this.recipeService.updateRecipe(recipeEntity2.getId(), this.recipeRequest2, 10002);

        //then
        assertThat(updatedRecipe, not(nullValue()));
        assertThat(updatedRecipe.getTitle(), equalTo(this.recipe2.getTitle()));
        assertThat(updatedRecipe.getImage(), equalTo(this.recipe2.getImage()));
        assertThat(updatedRecipe.getEstimatedTime(), equalTo(this.recipe2.getEstimatedTime()));
        assertThat(updatedRecipe.getDifficulty(), equalTo(this.recipe2.getDifficulty()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하지_않는_레시피_수정_Then_예외_발생() {
        //when
        this.recipeService.updateRecipe(2, this.recipeRequest2, 10002);
    }

    @Test(expected = NotExistDataException.class)
    public void When_재료_미등록_상태에서_레시피_수정_Then_예외_발생() {
        //given
        RecipeEntity recipeEntity = this.recipe2.toEntity();
        recipeEntity.setId(1);
        given(this.recipeRepository.findById(1)).willReturn(Optional.ofNullable(this.recipe1.toEntity()));

        //when
        this.recipeService.updateRecipe(recipeEntity.getId(), this.recipeRequest2, 10002);
    }

    @Test
    public void When_레시피_삭제_Then_이상_없음() {
        //when
        this.recipeService.deleteRecipe(1);

        //then
        assertThat(true, is(true));
    }

    @Test
    public void When_1건_조회_Then_카운트_1_반환() {
        //given
        given(this.recipeRepository.count()).willReturn(1L);

        //when
        final long recipeCnt = this.recipeService.readRecipeCnt();

        //then
        assertThat(recipeCnt, is(1L));
    }
}