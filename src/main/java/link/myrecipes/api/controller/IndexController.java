package link.myrecipes.api.controller;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class IndexController {

    @GetMapping("/")
    public ResponseEntity<ResourceSupport> index() {

        ResourceSupport resourceSupport = new ResourceSupport();
        resourceSupport.add(linkTo(UnitController.class).withRel("units-query"));
        resourceSupport.add(linkTo(MaterialController.class).withRel("materials-query"));
        resourceSupport.add(linkTo(RecipeController.class).withRel("recipes-query"));
        resourceSupport.add(linkTo(MemberController.class).withRel("members-query"));

        return ResponseEntity.ok(resourceSupport);
    }
}
