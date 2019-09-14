package io.myrecipes.api.service;

import io.myrecipes.api.domain.Recipe;
import io.myrecipes.api.repository.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceImplTest {
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Before
    public void setUp() {
        recipe1 = new Recipe("test1", "test1.jpg", 30, "1", 1001);
        recipe2 = new Recipe("test2", "test2.jpg", 60, "2", 1002);
        recipe3 = new Recipe("test3", "test3.jpg", 90, "3", 1003);
    }

    @Test
    public void Should_동일한_항목_반환_When_저장_성공() {
        given(recipeRepository.save(recipe1)).willReturn(recipe1);

        final Recipe savedRecipe = recipeService.createRecipe(recipe1);

        assertThat(savedRecipe, not(nullValue()));
        assertThat(savedRecipe.getTitle(), equalTo(recipe1.getTitle()));
        assertThat(savedRecipe.getImage(), equalTo(recipe1.getImage()));
        assertThat(savedRecipe.getEstimatedTime(), equalTo(recipe1.getEstimatedTime()));
        assertThat(savedRecipe.getDifficulty(), equalTo(recipe1.getDifficulty()));
    }

    @Test
    public void Should_리스트_반환_When_저장_성공() {
        List<Recipe> list = new ArrayList<>();
        list.add(recipe1);
        list.add(recipe2);
        list.add(recipe3);
        given(recipeRepository.findAll()).willReturn(list);

        final Page<Recipe> foundPage = recipeService.readRecipePageSortedByParam(0, 10, "registerDate", false);
        final List<Recipe> foundList = foundPage.getContent();

        assertThat(foundList.size(), is(3));
        assertThat(foundList.get(0).getTitle(), is("test1"));
        assertThat(foundList.get(1).getTitle(), is("test2"));
        assertThat(foundList.get(2).getTitle(), is("test3"));
    }

    @Test
    public void Should_업데이트된_항목_반환_When_업데이트_성공() {
        given(recipeRepository.getOne(1)).willReturn(recipe1);
        given(recipeRepository.save(any(Recipe.class))).willReturn(recipe2);

        final Recipe updatedRecipe = recipeService.updateRecipe(1, recipe2);

        assertThat(updatedRecipe, not(nullValue()));
        assertThat(updatedRecipe.getTitle(), equalTo(recipe2.getTitle()));
        assertThat(updatedRecipe.getImage(), equalTo(recipe2.getImage()));
        assertThat(updatedRecipe.getEstimatedTime(), equalTo(recipe2.getEstimatedTime()));
        assertThat(updatedRecipe.getDifficulty(), equalTo(recipe2.getDifficulty()));
    }

    @Test
    public void Should_Null_반환_When_업데이트_실패() {
        given(recipeRepository.getOne(1)).willReturn(null);

        final Recipe updatedRecipe = recipeService.updateRecipe(1, recipe2);

        assertThat(updatedRecipe, is(nullValue()));
    }
}