package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.common.LinkType;
import link.myrecipes.api.common.RestResource;
import link.myrecipes.api.dto.Material;
import link.myrecipes.api.service.MaterialService;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"material"})
@RestController
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping("/{id}")
    @ApiOperation("재료 조회")
    public ResponseEntity<RestResource<Material>> readMaterialList(@PathVariable int id) {

        Material material = this.materialService.readMaterial(id);

        RestResource<Material> restResource = new RestResource<>(material,
                String.valueOf(material.getId()),
                getClass(),
                new LinkType[] {LinkType.CREATE, LinkType.QUERY},
                "materials");
        restResource.add(new Link("/docs/index.html#resources-materials-read").withRel("profile"));

        return ResponseEntity.ok(restResource);
    }

    @GetMapping
    @ApiOperation("재료 리스트 조회")
    public ResponseEntity<Resources<Material>> readMaterialList() {

        List<Material> materialList = this.materialService.readMaterialList();

        Resources<Material> materialResources = new Resources<>(materialList);

        return ResponseEntity.ok(materialResources);
    }

    @PostMapping
    @ApiOperation("재료 저장")
    public ResponseEntity<RestResource<Material>> createMaterial(@RequestBody Material material, @RequestParam int userId) {

        Material savedMaterial = this.materialService.createMaterial(material, userId);

        RestResource<Material> restResource = new RestResource<>(savedMaterial,
                String.valueOf(savedMaterial.getId()),
                getClass(),
                new LinkType[] {LinkType.READ, LinkType.QUERY},
                "materials");
        restResource.add(new Link("/docs/index.html#resources-materials-create").withRel("profile"));

        return ResponseEntity.created(restResource.selfLink().getTemplate().expand()).body(restResource);
    }
}
