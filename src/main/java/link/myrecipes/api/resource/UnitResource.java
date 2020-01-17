package link.myrecipes.api.resource;

import link.myrecipes.api.controller.UnitController;
import link.myrecipes.api.dto.Unit;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class UnitResource extends Resource<Unit> {
    public UnitResource(Unit unit, Link... links) {
        super(unit, links);
        add(linkTo(UnitController.class).slash(unit.getName()).withSelfRel());
    }

    public Link selfLink() {
        return getLinks("self").get(0);
    }
}
