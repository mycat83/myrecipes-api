package io.myrecipes.api.controller;

import io.myrecipes.api.domain.Recipe;
import io.myrecipes.api.service.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"recipe"})
@RestController
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes/{id}")
    @ApiOperation("레시피 한건 조회")
    public ResponseEntity<Recipe> readRecipe(@PathVariable int id) {
        Recipe recipe = recipeService.readRecipe(id);
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @GetMapping("/recipes")
    @ApiOperation("레시피 페이지 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "string", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "페이지당 게시물 개수", dataType = "string", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sortField", value = "정렬기준 필드", dataType = "string", paramType = "query", defaultValue = "registerDate"),
            @ApiImplicitParam(name = "isDescending", value = "내림차순 정렬 여부", dataType = "boolean", paramType = "query", defaultValue = "false")
    })
    public ResponseEntity<Page<Recipe>> readRecipeList(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "registerDate") String sortField, @RequestParam(defaultValue = "false") boolean isDescending) {

        Page<Recipe> recipePage = recipeService.readRecipePageSortedByParam(page, size, sortField, isDescending);
        return new ResponseEntity<>(recipePage, HttpStatus.OK);
    }

    @PostMapping("/recipes")
    @ApiOperation("레시피 저장")
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        Recipe savedRecipe = recipeService.createRecipe(recipe);
        return new ResponseEntity<>(savedRecipe, HttpStatus.OK);
    }

    @PutMapping("/recipes/{id}")
    @ApiOperation("레시피 수정")
    public ResponseEntity<Object> updateRecipe(@PathVariable int id, @RequestBody Recipe recipe) {
        Recipe savedRecipe = recipeService.updateRecipe(id, recipe);
        return new ResponseEntity<>(savedRecipe, HttpStatus.OK);
    }

    @DeleteMapping("/recipes/{id}")
    @ApiOperation("레시피 삭제")
    public void deleteRecipe(@PathVariable int id) {
        recipeService.deleteRecipe(id);
    }
}
