package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.common.LinkType;
import link.myrecipes.api.common.RestResource;
import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.dto.RecipeCount;
import link.myrecipes.api.dto.request.RecipeRequest;
import link.myrecipes.api.dto.view.RecipeView;
import link.myrecipes.api.service.RecipeService;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(tags = {"recipe"})
@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    @ApiOperation("레시피 조회")
    public ResponseEntity<RestResource<RecipeView>> readRecipe(@PathVariable int id) {

        RecipeView recipeView = this.recipeService.readRecipe(id);

        RestResource<RecipeView> restResource = new RestResource<>(recipeView,
                String.valueOf(recipeView.getId()),
                getClass(),
                new LinkType[] {LinkType.CREATE, LinkType.UPDATE, LinkType.DELETE, LinkType.QUERY},
                "recipes");
        restResource.add(new Link("/docs/index.html#resources-recipes-read").withRel("profile"));

        return ResponseEntity.ok(restResource);
    }

    @GetMapping
    @ApiOperation("레시피 페이지 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "string", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "페이지당 게시물 개수", dataType = "string", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sortField", value = "정렬기준 필드", dataType = "string", paramType = "query", defaultValue = "registerDate"),
            @ApiImplicitParam(name = "isDescending", value = "내림차순 정렬 여부", dataType = "boolean", paramType = "query", defaultValue = "false")
    })
    public ResponseEntity<List<Recipe>> readRecipeList(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "registerDate") String sortField, @RequestParam(defaultValue = "false") boolean isDescending) {

        List<Recipe> recipePage = this.recipeService.readRecipePageSortedByParam(page, size, sortField, isDescending);
        return new ResponseEntity<>(recipePage, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("레시피 저장")
    public ResponseEntity<RestResource<Recipe>> createRecipe(@RequestBody @Valid RecipeRequest recipeRequest,
                                                             @RequestParam int userId) {

        Recipe savedRecipe = this.recipeService.createRecipe(recipeRequest, userId);

        RestResource<Recipe> restResource = new RestResource<>(savedRecipe,
                String.valueOf(savedRecipe.getId()),
                getClass(),
                new LinkType[] {LinkType.READ, LinkType.UPDATE, LinkType.DELETE, LinkType.QUERY},
                "recipes");
        restResource.add(new Link("/docs/index.html#resources-recipes-create").withRel("profile"));

        return ResponseEntity.created(restResource.selfLink().getTemplate().expand()).body(restResource);
    }

    @PutMapping("/{id}")
    @ApiOperation("레시피 수정")
    public ResponseEntity<RestResource<Recipe>> updateRecipe(@PathVariable int id,
                                                             @RequestBody @Valid RecipeRequest recipeRequest,
                                                             @RequestParam int userId) {

        Recipe savedRecipe = this.recipeService.updateRecipe(id, recipeRequest, userId);

        RestResource<Recipe> restResource = new RestResource<>(savedRecipe,
                String.valueOf(savedRecipe.getId()),
                getClass(),
                new LinkType[] {LinkType.CREATE, LinkType.READ, LinkType.DELETE, LinkType.QUERY},
                "recipes");
        restResource.add(new Link("/docs/index.html#resources-recipes-update").withRel("profile"));

        return ResponseEntity.ok(restResource);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("레시피 삭제")
    public void deleteRecipe(@PathVariable int id) {

        this.recipeService.deleteRecipe(id);

//        RestResource<Recipe> restResource = new RestResource<>(null,
//                null,
//                getClass(),
//                new LinkType[] {LinkType.CREATE, LinkType.READ, LinkType.DELETE, LinkType.QUERY},
//                "recipe");
//        restResource.add(new Link("/docs/index.html#resources-delete-recipe").withRel("profile"));
//
//        return ResponseEntity.ok(restResource);
    }

    @GetMapping("/count")
    @ApiOperation("레시피 건 수 조회")
    public ResponseEntity<RestResource<RecipeCount>> recipeCount() {
        RecipeCount recipeCount = this.recipeService.readRecipeCount();

        RestResource<RecipeCount> restResource = new RestResource<>(recipeCount);
        restResource.add(linkTo(methodOn(getClass()).recipeCount()).withSelfRel());
        restResource.add(linkTo(getClass()).withRel("recipes-query"));
        restResource.add(new Link("/docs/index.html#resources-recipes-count").withRel("profile"));

        return ResponseEntity.ok(restResource);
    }

    @PutMapping("/{id}/readCount")
    @ApiOperation("레시피 조회수 증가")
    public void increaseReadCount(@PathVariable int id) {
        this.recipeService.increaseReadCount(id);
    }

    @GetMapping("/popular")
    @ApiOperation("인기 레시피 리스트 조회")
    public ResponseEntity<List<Recipe>> readPopularRecipeList() {
        List<Recipe> popularRecipesDocumentList = this.recipeService.readPopularRecipeList();
        return new ResponseEntity<>(popularRecipesDocumentList, HttpStatus.OK);
    }
}
