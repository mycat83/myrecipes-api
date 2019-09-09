package io.myrecipes.api.service;

import io.myrecipes.api.domain.Recipe;
import io.myrecipes.api.repository.RecipeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
        recipe1 = new Recipe("test1", "test1.jpg", 30, "1");
        recipe2 = new Recipe("test2", "test2.jpg", 60, "2");
        recipe3 = new Recipe("test3", "test3.jpg", 90, "3");
    }

    @Test
    public void Should_동일한_값_반환_When_저장_성공() {
        given(recipeRepository.save(recipe1)).willReturn(recipe1);

        final Recipe savedRecipe1 = recipeService.createRecipe(recipe1);

        assertThat(savedRecipe1, not(nullValue()));
        assertThat(savedRecipe1.getTitle(), equalTo(recipe1.getTitle()));
        assertThat(savedRecipe1.getImage(), equalTo(recipe1.getImage()));
        assertThat(savedRecipe1.getEstimatedTime(), equalTo(recipe1.getEstimatedTime()));
        assertThat(savedRecipe1.getDifficulty(), equalTo(recipe1.getDifficulty()));
    }

}