package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.dto.request.RecipeRequest;
import link.myrecipes.api.dto.view.RecipeView;
import link.myrecipes.api.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"recipe"})
@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    @ApiOperation("레시피 한건 조회")
    public ResponseEntity<RecipeView> readRecipe(@PathVariable int id) {
        RecipeView recipeView = this.recipeService.readRecipe(id);
        return new ResponseEntity<>(recipeView, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation("레시피 페이지 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "string", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "페이지당 게시물 개수", dataType = "string", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sortField", value = "정렬기준 필드", dataType = "string", paramType = "query", defaultValue = "registerDate"),
            @ApiImplicitParam(name = "isDescending", value = "내림차순 정렬 여부", dataType = "boolean", paramType = "query", defaultValue = "false")
    })
    public ResponseEntity<List<Recipe>> readRecipeList(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "registerDate") String sortField, @RequestParam(defaultValue = "false") boolean isDescending) {

        List<Recipe> recipePage = this.recipeService.readRecipePageSortedByParam(page - 1, size, sortField, isDescending);
        return new ResponseEntity<>(recipePage, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("레시피 저장")
    public ResponseEntity<Recipe> createRecipe(@RequestBody @Valid RecipeRequest recipeRequest, @RequestParam int userId) {
        Recipe savedRecipe = this.recipeService.createRecipe(recipeRequest, userId);
        return new ResponseEntity<>(savedRecipe, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation("레시피 수정")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable int id, @RequestBody @Valid RecipeRequest recipeRequest, @RequestParam int userId) {
        Recipe savedRecipe = this.recipeService.updateRecipe(id, recipeRequest, userId);
        return new ResponseEntity<>(savedRecipe, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("레시피 삭제")
    public void deleteRecipe(@PathVariable int id) {
        this.recipeService.deleteRecipe(id);
    }

    @GetMapping("/cnt")
    @ApiOperation("레시피 건 수 조회")
    public ResponseEntity<Long> readRecipe() {
        long recipeCnt = this.recipeService.readRecipeCnt();
        return new ResponseEntity<>(recipeCnt, HttpStatus.OK);
    }
}
