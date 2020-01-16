package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.service.UnitService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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

        Resource<Unit> unitResource = new Resource<>(unit);
        ControllerLinkBuilder selfLinkBuilder = linkTo(methodOn(UnitController.class).readUnit(name));
        unitResource.add(selfLinkBuilder.withSelfRel());
        unitResource.add(linkTo(methodOn(UnitController.class).createUnit(unit, 1001)).withRel("insert-unit"));

        return ResponseEntity.ok(unitResource);
    }


    @PostMapping
    @ApiOperation("단위 저장")
    public ResponseEntity<Unit> createUnit(@RequestBody Unit unit, @RequestParam int userId) {

        Unit savedUnit = this.unitService.createUnit(unit, userId);
        return new ResponseEntity<>(savedUnit, HttpStatus.OK);
    }
}
