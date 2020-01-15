package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.dto.Material;
import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.service.BaseInfoService;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(tags = {"baseInfo"})
@RestController
public class BaseInfoController {

    private final BaseInfoService baseInfoService;

    public BaseInfoController(BaseInfoService baseInfoService) {
        this.baseInfoService = baseInfoService;
    }

    @GetMapping("/materials")
    @ApiOperation("재료 리스트 조회")
    public ResponseEntity<Resources<Material>> readMaterialList() {

        List<Material> materialList = this.baseInfoService.readMaterialList();

        Resources<Material> materialResources = new Resources<>(materialList);
        ControllerLinkBuilder selfLinkBuilder = linkTo(methodOn(BaseInfoController.class).readMaterialList());
        materialResources.add(selfLinkBuilder.withSelfRel());

        return ResponseEntity.ok(materialResources);
    }

    @PostMapping("/materials")
    @ApiOperation("재료 저장")
    public ResponseEntity<Material> createMaterial(@RequestBody Material material, @RequestParam int userId) {

        Material savedMaterial = this.baseInfoService.createMaterial(material, userId);
        return new ResponseEntity<>(savedMaterial, HttpStatus.OK);
    }

    @PostMapping("/units")
    @ApiOperation("단위 저장")
    public ResponseEntity<Unit> createUnit(@RequestBody Unit unit, @RequestParam int userId) {

        Unit savedUnit = this.baseInfoService.createUnit(unit, userId);
        return new ResponseEntity<>(savedUnit, HttpStatus.OK);
    }
}
