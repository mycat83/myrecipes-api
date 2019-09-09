package io.myrecipes.api.controller;

import io.myrecipes.api.domain.Recipe;
import io.myrecipes.api.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<Recipe> readRecipe(@PathVariable int id) {
        Recipe recipe = recipeService.readRecipe(id);
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<Recipe>> readRecipeList() {
        List<Recipe> recipeList = recipeService.readRecipeList();
        return new ResponseEntity<>(recipeList, HttpStatus.OK);
    }

    @PostMapping("/recipes")
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        Recipe savedRecipe = recipeService.createRecipe(recipe);
        return new ResponseEntity<>(savedRecipe, HttpStatus.OK);
    }

    @PutMapping("/recipes/{id}")
    public ResponseEntity<Object> updateRecipe(@PathVariable int id, @RequestBody Recipe recipe) {
        Recipe savedRecipe = recipeService.updateRecipe(id, recipe);
        return new ResponseEntity<>(savedRecipe, HttpStatus.OK);
    }

    @DeleteMapping("/recipes/{id}")
    public void deleteRecipe(@PathVariable int id) {
        recipeService.deleteRecipe(id);
    }
}
