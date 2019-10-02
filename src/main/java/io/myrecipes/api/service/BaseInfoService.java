package io.myrecipes.api.service;

import io.myrecipes.api.dto.Material;
import io.myrecipes.api.dto.Unit;

public interface BaseInfoService {
    Material readMaterial(int id);

    Material createMaterial(Material material);

    Unit readUnit(String name);

    Unit createUnit(Unit unit);

}
