package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.common.LinkType;
import link.myrecipes.api.common.RestResource;
import link.myrecipes.api.dto.Material;
import link.myrecipes.api.service.MaterialService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Api(tags = {"material"})
@RestController
@RequestMapping("/materials")
public class MaterialController {

    private static final String MATERIALS = "materials";
    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping("/{id}")
    @ApiOperation("재료 조회")
    public ResponseEntity<ResourceSupport> readMaterialList(@PathVariable int id) {

        Material material = this.materialService.readMaterial(id);

        RestResource<Material> restResource = new RestResource<>(material,
                String.valueOf(material.getId()),
                getClass(),
                new LinkType[] {LinkType.CREATE, LinkType.QUERY},
                MATERIALS);
        restResource.addProfileLink("/docs/index.html#resources-materials-read");

        return ResponseEntity.ok(restResource);
    }

    @GetMapping
    @ApiOperation("재료 리스트 조회")
    public ResponseEntity<ResourceSupport> readMaterialList(Pageable pageable, PagedResourcesAssembler<Material> assembler) {

        Page<Material> materialPage = this.materialService.readMaterialList(pageable);

        PagedResources<RestResource<Material>> pagedResources = assembler.toResource(materialPage, material ->
                new RestResource<>(material,
                        String.valueOf(material.getId()),
                        getClass(),
                        new LinkType[] {},
                        MATERIALS));
        pagedResources.add(linkTo(getClass()).withRel("materials-create"));
        pagedResources.add(new Link("/docs/index.html#resources-materials-query").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    @PostMapping
    @ApiOperation("재료 저장")
    public ResponseEntity<ResourceSupport> createMaterial(@RequestBody Material material, @RequestParam int userId) {

        Material savedMaterial = this.materialService.createMaterial(material, userId);

        RestResource<Material> restResource = new RestResource<>(savedMaterial,
                String.valueOf(savedMaterial.getId()),
                getClass(),
                new LinkType[] {LinkType.READ, LinkType.QUERY},
                MATERIALS);
        restResource.addProfileLink("/docs/index.html#resources-materials-create");

        return ResponseEntity.created(restResource.selfLink().getTemplate().expand()).body(restResource);
    }
}
