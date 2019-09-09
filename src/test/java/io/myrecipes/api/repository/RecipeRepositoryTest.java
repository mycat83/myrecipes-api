package io.myrecipes.api.repository;

import io.myrecipes.api.domain.Recipe;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RecipeRepositoryTest {
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;

    @Autowired
    private RecipeRepository recipeRepository;

    @Before
    public void setUp() {
        recipe1 = new Recipe("test1", "test1.jpg", 30, "1");
        recipe2 = new Recipe("test2", "test2.jpg", 60, "2");
        recipe3 = new Recipe("test3", "test3.jpg", 90, "3");
    }

    @Test
    public void Should_동일한_엔티티_반환_When_엔티티_저장() {
        recipeRepository.save(recipe1);
        List<Recipe> recipeList = recipeRepository.findAll();

        assertThat(recipeList.size(), is(1));
        assertThat(recipeList.get(0).getTitle(), is("test1"));
        assertThat(recipeList.get(0).getImage(), is("test1.jpg"));
        assertThat(recipeList.get(0).getEstimatedTime(), is(30));
        assertThat(recipeList.get(0).getDifficulty(), is("1"));

        recipe2.setId(recipeList.get(0).getId());
        recipeRepository.save(recipe2);
        List<Recipe> updatedRecipeList = recipeRepository.findAll();

        assertThat(updatedRecipeList.size(), is(1));
        assertThat(updatedRecipeList.get(0).getTitle(), is("test2"));
        assertThat(updatedRecipeList.get(0).getImage(), is("test2.jpg"));
        assertThat(updatedRecipeList.get(0).getEstimatedTime(), is(60));
        assertThat(updatedRecipeList.get(0).getDifficulty(), is("2"));
    }

    @Test
    public void Should_키_순차적_증가_When_엔티티_여러개_저장() {
        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);
        recipeRepository.save(recipe3);
        List<Recipe> recipeList = recipeRepository.findAll();

        assertThat(recipeList.size(), is(3));
        assertThat(recipeList.get(0).getId(), is(1));
        assertThat(recipeList.get(1).getId(), is(2));
        assertThat(recipeList.get(2).getId(), is(3));
    }

    @Test
    public void Should_엔티티_없음_When_엔티티_저장후_삭제() {
        recipeRepository.save(recipe1);
        recipeRepository.deleteAll();

        assertThat(recipeRepository.findAll(), IsEmptyCollection.empty());
    }
}