package link.myrecipes.api.service;

import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.dto.request.UnitRequest;

public interface UnitService {

    Unit readUnit(String name);

    Unit createUnit(UnitRequest unit, int userId);
}
