package link.myrecipes.api.service;

import link.myrecipes.api.dto.Unit;

public interface UnitService {

    Unit readUnit(String name);

    Unit createUnit(Unit unit, int userId);
}
