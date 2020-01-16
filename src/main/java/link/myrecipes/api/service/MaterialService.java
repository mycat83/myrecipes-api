package link.myrecipes.api.service;

import link.myrecipes.api.dto.Material;

import java.util.List;

public interface MaterialService {

    Material readMaterial(int id);

    List<Material> readMaterialList();

    Material createMaterial(Material material, int userId);
}
