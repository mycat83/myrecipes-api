package link.myrecipes.api.service;

import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.repository.UnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;

    public UnitServiceImpl(UnitRepository unitRepository) {

        this.unitRepository = unitRepository;
    }

    @Override
    public Unit readUnit(String name) {

        Optional<UnitEntity> unitEntityOptional = this.unitRepository.findByName(name);
        if (unitEntityOptional.isEmpty()) {
            throw new NotExistDataException(UnitEntity.class, name);
        }

        return unitEntityOptional.get().toDTO();
    }

    @Override
    @Transactional
    public Unit createUnit(Unit unit, int userId) {

        UnitEntity unitEntity = unit.toEntity();
        unitEntity.setRegisterUserId(userId);

        return this.unitRepository.save(unitEntity).toDTO();
    }
}
