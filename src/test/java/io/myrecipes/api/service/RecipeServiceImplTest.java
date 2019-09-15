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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
        given(recipeRepository.save(this.recipe1)).willReturn(this.recipe1);

        final Recipe savedRecipe = this.recipeService.createRecipe(this.recipe1);

        assertThat(savedRecipe, not(nullValue()));
        assertThat(savedRecipe.getTitle(), equalTo(this.recipe1.getTitle()));
        assertThat(savedRecipe.getImage(), equalTo(this.recipe1.getImage()));
        assertThat(savedRecipe.getEstimatedTime(), equalTo(this.recipe1.getEstimatedTime()));
        assertThat(savedRecipe.getDifficulty(), equalTo(this.recipe1.getDifficulty()));
    }

    @Test
    public void Should_리스트_반환_When_저장_성공() {
        List<Recipe> list = new ArrayList<>();
        list.add(this.recipe1);
        list.add(this.recipe2);
        list.add(this.recipe3);

        Page<Recipe> page = new PageImpl<>(list, PageRequest.of(0, list.size()), list.size());
        given(this.recipeRepository.findAll(any(PageRequest.class))).willReturn(page);

        final List<Recipe> foundList = this.recipeService.readRecipePageSortedByParam(0, 10, "registerDate", false);

        assertThat(foundList.size(), is(3));
        assertThat(foundList.get(0).getTitle(), is("test1"));
        assertThat(foundList.get(1).getTitle(), is("test2"));
        assertThat(foundList.get(2).getTitle(), is("test3"));
    }

    @Test
    public void Should_업데이트된_항목_반환_When_업데이트_성공() {
        given(recipeRepository.getOne(1)).willReturn(this.recipe1);
        given(recipeRepository.save(any(Recipe.class))).willReturn(this.recipe2);

        final Recipe updatedRecipe = this.recipeService.updateRecipe(1, this.recipe2);

        assertThat(updatedRecipe, not(nullValue()));
        assertThat(updatedRecipe.getTitle(), equalTo(this.recipe2.getTitle()));
        assertThat(updatedRecipe.getImage(), equalTo(this.recipe2.getImage()));
        assertThat(updatedRecipe.getEstimatedTime(), equalTo(this.recipe2.getEstimatedTime()));
        assertThat(updatedRecipe.getDifficulty(), equalTo(this.recipe2.getDifficulty()));
    }

    @Test
    public void Should_Null_반환_When_업데이트_실패() {
        given(recipeRepository.getOne(1)).willReturn(null);

        final Recipe updatedRecipe = this.recipeService.updateRecipe(1, this.recipe2);

        assertThat(updatedRecipe, is(nullValue()));
    }
}