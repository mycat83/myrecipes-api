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
        resourceSupport.add(linkTo(UnitController.class).withRel("units"));
        resourceSupport.add(linkTo(MaterialController.class).withRel("materials"));
        resourceSupport.add(linkTo(RecipeController.class).withRel("recipes"));
        resourceSupport.add(linkTo(MemberController.class).withRel("members"));

        return ResponseEntity.ok(resourceSupport);
    }
}