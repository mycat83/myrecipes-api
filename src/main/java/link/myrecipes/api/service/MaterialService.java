package link.myrecipes.api.service;

import link.myrecipes.api.dto.Material;
import link.myrecipes.api.dto.request.MaterialRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaterialService {

    Material readMaterial(int id);

    Page<Material> readMaterialList(Pageable pageable);

    Material createMaterial(MaterialRequest material, int userId);
}
