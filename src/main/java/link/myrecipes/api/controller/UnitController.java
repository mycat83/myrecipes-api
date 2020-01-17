package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.resource.UnitResource;
import link.myrecipes.api.service.UnitService;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Api(tags = {"unit"})
@RestController
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping("/{name}")
    @ApiOperation("단위 조회")
    public ResponseEntity<Resource<Unit>> readUnit(@PathVariable String name) {

        Unit unit = this.unitService.readUnit(name);

        UnitResource unitResource = new UnitResource(unit);
        unitResource.add(linkTo(UnitController.class).withRel("create-unit"));
        unitResource.add(new Link("/docs/index.html#resources-read-unit").withRel("profile"));

        return ResponseEntity.ok(unitResource);
    }


    @PostMapping
    @ApiOperation("단위 저장")
    public ResponseEntity<Resource<Unit>> createUnit(@RequestBody Unit unit, @RequestParam int userId) {

        Unit savedUnit = this.unitService.createUnit(unit, userId);

        UnitResource unitResource = new UnitResource(savedUnit);
        unitResource.add(unitResource.selfLink().withRel("read-unit"));
        unitResource.add(new Link("/docs/index.html#resources-create-unit").withRel("profile"));

        return ResponseEntity.created(unitResource.selfLink().getTemplate().expand()).body(unitResource);
    }
}
