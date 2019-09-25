package io.myrecipes.api.service;

import io.myrecipes.api.domain.Recipe;
import io.myrecipes.api.dto.RecipeDTO;
import io.myrecipes.api.repository.RecipeRepository;
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
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceImplTest {
    private RecipeDTO recipeDTO1;
    private RecipeDTO recipeDTO2;
    private RecipeDTO recipeDTO3;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Before
    public void setUp() {
        this.recipeDTO1 = RecipeDTO.builder().title("test1").image("image1.jpg").estimatedTime(30).difficulty(1).build();
        this.recipeDTO2 = RecipeDTO.builder().title("test2").image("image2.jpg").estimatedTime(60).difficulty(3).build();
        this.recipeDTO3 = RecipeDTO.builder().title("test2").image("image3.jpg").estimatedTime(90).difficulty(5).build();
    }

    @Test
    public void Should_동일한_항목_반환_When_저장_성공() {
        given(this.recipeRepository.save(any(Recipe.class))).willReturn(this.recipeDTO1.toDomain());

        final RecipeDTO savedRecipeDTO = this.recipeService.createRecipe(this.recipeDTO1);

        assertThat(savedRecipeDTO, not(nullValue()));
        assertThat(savedRecipeDTO.getTitle(), equalTo(this.recipeDTO1.getTitle()));
        assertThat(savedRecipeDTO.getImage(), equalTo(this.recipeDTO1.getImage()));
        assertThat(savedRecipeDTO.getEstimatedTime(), equalTo(this.recipeDTO1.getEstimatedTime()));
        assertThat(savedRecipeDTO.getDifficulty(), equalTo(this.recipeDTO1.getDifficulty()));
    }

    @Test
    public void Should_리스트_반환_When_저장_성공() {
        List<RecipeDTO> list = new ArrayList<>();
        list.add(this.recipeDTO1);
        list.add(this.recipeDTO2);
        list.add(this.recipeDTO3);

        Page<Recipe> page = new PageImpl<>(
                list.stream().map(RecipeDTO::toDomain).collect(Collectors.toList()),
                PageRequest.of(0, list.size()), list.size()
        );
        given(this.recipeRepository.findAll(any(PageRequest.class))).willReturn(page);

        final List<RecipeDTO> foundList = this.recipeService.readRecipePageSortedByParam(0, 10, "registerDate", false);

        assertThat(foundList.size(), is(3));
        assertThat(foundList.get(0).getTitle(), is(this.recipeDTO1.getTitle()));
        assertThat(foundList.get(1).getTitle(), is(this.recipeDTO2.getTitle()));
        assertThat(foundList.get(2).getTitle(), is(this.recipeDTO3.getTitle()));
    }

    @Test
    public void Should_업데이트된_항목_반환_When_업데이트_성공() {
        given(this.recipeRepository.getOne(1)).willReturn(this.recipeDTO1.toDomain());
        given(this.recipeRepository.save(any(Recipe.class))).willReturn(this.recipeDTO2.toDomain());

        final RecipeDTO updatedRecipe = this.recipeService.updateRecipe(1, this.recipeDTO2);

        assertThat(updatedRecipe, not(nullValue()));
        assertThat(updatedRecipe.getTitle(), equalTo(this.recipeDTO2.getTitle()));
        assertThat(updatedRecipe.getImage(), equalTo(this.recipeDTO2.getImage()));
        assertThat(updatedRecipe.getEstimatedTime(), equalTo(this.recipeDTO2.getEstimatedTime()));
        assertThat(updatedRecipe.getDifficulty(), equalTo(this.recipeDTO2.getDifficulty()));
    }

    @Test
    public void Should_Null_반환_When_업데이트_실패() {
        given(this.recipeRepository.getOne(1)).willReturn(null);

        final RecipeDTO updatedRecipe = this.recipeService.updateRecipe(1, this.recipeDTO2);

        assertThat(updatedRecipe, is(nullValue()));
    }
}