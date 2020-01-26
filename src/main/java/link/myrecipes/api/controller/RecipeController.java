package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.common.LinkType;
import link.myrecipes.api.common.RestResource;
import link.myrecipes.api.common.RestResourceSupport;
import link.myrecipes.api.common.RestResources;
import link.myrecipes.api.dto.Recipe;
import link.myrecipes.api.dto.RecipeCount;
import link.myrecipes.api.dto.request.RecipeRequest;
import link.myrecipes.api.dto.view.RecipeView;
import link.myrecipes.api.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
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

    private static final String RECIPES = "recipes";
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    @ApiOperation("레시피 조회")
    public ResponseEntity<ResourceSupport> readRecipe(@PathVariable int id) {

        RecipeView recipeView = this.recipeService.readRecipe(id);

        RestResource<RecipeView> restResource = new RestResource<>(recipeView,
                String.valueOf(recipeView.getId()),
                getClass(),
                new LinkType[] {LinkType.CREATE, LinkType.UPDATE, LinkType.DELETE, LinkType.QUERY},
                RECIPES);
        restResource.addProfileLink("/docs/index.html#resources-recipes-read");

        return ResponseEntity.ok(restResource);
    }

    @GetMapping
    @ApiOperation("레시피 리스트 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "페이지당 게시물 개수", dataType = "integer", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "정렬기준 필드", allowMultiple = true, dataType = "string", paramType = "query", defaultValue = "registerDate,ASC")
    })
    public ResponseEntity<ResourceSupport> readRecipeList(Pageable pageable, PagedResourcesAssembler<Recipe> assembler) {

        Page<Recipe> recipePage = this.recipeService.readRecipeList(pageable);

        PagedResources<RestResource<Recipe>> pagedResources = assembler.toResource(recipePage, recipe ->
                new RestResource<>(recipe,
                        String.valueOf(recipe.getId()),
                        getClass(),
                        new LinkType[] {},
                        RECIPES));
        pagedResources.add(linkTo(getClass()).withRel("recipes-create"));
        pagedResources.add(new Link("/docs/index.html#resources-recipes-query").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    @PostMapping
    @ApiOperation("레시피 저장")
    public ResponseEntity<ResourceSupport> createRecipe(@RequestBody @Valid RecipeRequest recipeRequest,
                                                        @RequestParam int userId) {

        Recipe savedRecipe = this.recipeService.createRecipe(recipeRequest, userId);

        RestResource<Recipe> restResource = new RestResource<>(savedRecipe,
                String.valueOf(savedRecipe.getId()),
                getClass(),
                new LinkType[] {LinkType.READ, LinkType.UPDATE, LinkType.DELETE, LinkType.QUERY},
                RECIPES);
        restResource.addProfileLink("/docs/index.html#resources-recipes-create");

        return ResponseEntity.created(restResource.selfLink().getTemplate().expand()).body(restResource);
    }

    @PutMapping("/{id}")
    @ApiOperation("레시피 수정")
    public ResponseEntity<ResourceSupport> updateRecipe(@PathVariable int id,
                                                             @RequestBody @Valid RecipeRequest recipeRequest,
                                                             @RequestParam int userId) {

        Recipe savedRecipe = this.recipeService.updateRecipe(id, recipeRequest, userId);

        RestResource<Recipe> restResource = new RestResource<>(savedRecipe,
                String.valueOf(savedRecipe.getId()),
                getClass(),
                new LinkType[] {LinkType.CREATE, LinkType.READ, LinkType.DELETE, LinkType.QUERY},
                RECIPES);
        restResource.addProfileLink("/docs/index.html#resources-recipes-update");

        return ResponseEntity.ok(restResource);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("레시피 삭제")
    public ResponseEntity<ResourceSupport> deleteRecipe(@PathVariable int id) {

        this.recipeService.deleteRecipe(id);

        RestResourceSupport restResourceSupport = new RestResourceSupport(
                getClass(),
                new LinkType[] {LinkType.QUERY},
                RECIPES);
        restResourceSupport.add(linkTo(methodOn(getClass()).recipeCount()).withSelfRel());
        restResourceSupport.addProfileLink("/docs/index.html#resources-recipes-delete");

        return ResponseEntity.ok(restResourceSupport);
    }

    @GetMapping("/count")
    @ApiOperation("레시피 건 수 조회")
    public ResponseEntity<ResourceSupport> recipeCount() {

        RecipeCount recipeCount = this.recipeService.readRecipeCount();

        RestResource<RecipeCount> restResource = new RestResource<>(recipeCount);
        restResource.add(linkTo(methodOn(getClass()).recipeCount()).withSelfRel());
        restResource.add(linkTo(getClass()).withRel("recipes-query"));
        restResource.addProfileLink("/docs/index.html#resources-recipes-count");

        return ResponseEntity.ok(restResource);
    }

    @PutMapping("/{id}/readCount")
    @ApiOperation("레시피 조회수 증가")
    public ResponseEntity<ResourceSupport> increaseReadCount(@PathVariable int id) {

        this.recipeService.increaseReadCount(id);

        RestResourceSupport restResourceSupport = new RestResourceSupport(
                getClass(),
                new LinkType[] {LinkType.QUERY},
                RECIPES);
        restResourceSupport.add(linkTo(methodOn(getClass()).recipeCount()).withSelfRel());
        restResourceSupport.addProfileLink("/docs/index.html#resources-recipes-readCount");

        return ResponseEntity.ok(restResourceSupport);
    }

    @GetMapping("/popular")
    @ApiOperation("인기 레시피 리스트 조회")
    public ResponseEntity<ResourceSupport> readPopularRecipeList() {

        List<Recipe> recipeList = this.recipeService.readPopularRecipeList();

        RestResources<Recipe> recipeResources = new RestResources<>(recipeList,
                getClass(),
                new LinkType[] {LinkType.CREATE},
                RECIPES);
        recipeResources.add(linkTo(methodOn(getClass()).recipeCount()).withSelfRel());
        recipeResources.addProfileLink("/docs/index.html#resources-recipes-popular");

        return ResponseEntity.ok(recipeResources);
    }
}
