package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.common.LinkType;
import link.myrecipes.api.common.RestResource;
import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.service.UnitService;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"unit"})
@RestController
@RequestMapping("/units")
public class UnitController {

    private static final String UNITS = "units";
    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping("/{name}")
    @ApiOperation("단위 조회")
    public ResponseEntity<ResourceSupport> readUnit(@PathVariable String name) {

        Unit unit = this.unitService.readUnit(name);

        RestResource<Unit> restResource = new RestResource<>(unit,
                unit.getName(),
                getClass(),
                new LinkType[] {LinkType.CREATE},
                UNITS);
        restResource.addProfileLink("/docs/index.html#resources-units-read");

        return ResponseEntity.ok(restResource);
    }

    @PostMapping
    @ApiOperation("단위 저장")
    public ResponseEntity<ResourceSupport> createUnit(@RequestBody Unit unit, @RequestParam int userId) {

        Unit savedUnit = this.unitService.createUnit(unit, userId);

        RestResource<Unit> restResource = new RestResource<>(savedUnit,
                savedUnit.getName(),
                getClass(),
                new LinkType[] {LinkType.READ},
                UNITS);
        restResource.addProfileLink("/docs/index.html#resources-units-read");

        return ResponseEntity.created(restResource.selfLink().getTemplate().expand()).body(restResource);
    }
}
