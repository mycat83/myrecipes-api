package link.myrecipes.api.service;

import link.myrecipes.api.dto.Material;
import link.myrecipes.api.dto.Unit;

import java.util.List;

public interface BaseInfoService {
    Material readMaterial(int id);

    List<Material> readMaterialList();

    Material createMaterial(Material material, int userId);

    Unit readUnit(String name);

    Unit createUnit(Unit unit, int userId);
}
