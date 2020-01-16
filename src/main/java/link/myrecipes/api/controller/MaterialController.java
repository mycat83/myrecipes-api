package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.dto.Material;
import link.myrecipes.api.service.MaterialService;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(tags = {"material"})
@RestController
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    @ApiOperation("재료 리스트 조회")
    public ResponseEntity<Resources<Material>> readMaterialList() {

        List<Material> materialList = this.materialService.readMaterialList();

        Resources<Material> materialResources = new Resources<>(materialList);
        ControllerLinkBuilder selfLinkBuilder = linkTo(methodOn(MaterialController.class).readMaterialList());
        materialResources.add(selfLinkBuilder.withSelfRel());

        return ResponseEntity.ok(materialResources);
    }

    @PostMapping
    @ApiOperation("재료 저장")
    public ResponseEntity<Material> createMaterial(@RequestBody Material material, @RequestParam int userId) {

        Material savedMaterial = this.materialService.createMaterial(material, userId);
        return new ResponseEntity<>(savedMaterial, HttpStatus.OK);
    }
}
