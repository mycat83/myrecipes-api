package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.common.LinkType;
import link.myrecipes.api.common.RestResource;
import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.service.UnitService;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<RestResource<Unit>> readUnit(@PathVariable String name) {

        Unit unit = this.unitService.readUnit(name);

        RestResource<Unit> restResource = new RestResource<>(unit,
                unit.getName(),
                getClass(),
                new LinkType[] {LinkType.CREATE},
                "unit");
        restResource.add(new Link("/docs/index.html#resources-read-unit").withRel("profile"));

        return ResponseEntity.ok(restResource);
    }


    @PostMapping
    @ApiOperation("단위 저장")
    public ResponseEntity<RestResource<Unit>> createUnit(@RequestBody Unit unit, @RequestParam int userId) {

        Unit savedUnit = this.unitService.createUnit(unit, userId);

        RestResource<Unit> restResource = new RestResource<>(savedUnit,
                savedUnit.getName(),
                getClass(),
                new LinkType[] {LinkType.READ},
                "unit");
        restResource.add(new Link("/docs/index.html#resources-read-unit").withRel("profile"));

        return ResponseEntity.created(restResource.selfLink().getTemplate().expand()).body(restResource);
    }
}
