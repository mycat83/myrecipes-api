package link.myrecipes.api.service;

import link.myrecipes.api.dto.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaterialService {

    Material readMaterial(int id);

    Page<Material> readMaterialList(Pageable pageable);

    Material createMaterial(Material material, int userId);
}
