package io.myrecipes.api.service;

import io.myrecipes.api.domain.MaterialEntity;
import io.myrecipes.api.domain.UnitEntity;
import io.myrecipes.api.dto.Material;
import io.myrecipes.api.dto.Unit;
import io.myrecipes.api.exception.NotExistDataException;
import io.myrecipes.api.repository.MaterialRepository;
import io.myrecipes.api.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BaseInfoServiceImpl implements BaseInfoService {
    private final MaterialRepository materialRepository;
    private final UnitRepository unitRepository;

    public BaseInfoServiceImpl(MaterialRepository materialRepository, UnitRepository unitRepository) {
        this.materialRepository = materialRepository;
        this.unitRepository = unitRepository;
    }

    @Override
    public Material readMaterial(int id) {
        Optional<MaterialEntity> materialEntityOptional = this.materialRepository.findById(id);

        if (!materialEntityOptional.isPresent()) {
            throw new NotExistDataException("MaterialEntity", id);
        }

        return materialEntityOptional.get().toDTO();
    }

    @Override
    public Material createMaterial(Material material) {
        MaterialEntity materialEntity = material.toEntity();

        Optional<UnitEntity> unitEntityOptional = this.unitRepository.findByName(material.getUnitName());
        if (!unitEntityOptional.isPresent()) {
            throw new NotExistDataException("UnitEntity", material.getUnitName());
        }

        materialEntity.setUnitEntity(unitEntityOptional.get());
        return this.materialRepository.save(materialEntity).toDTO();
    }

    @Override
    public Unit readUnit(String name) {
        Optional<UnitEntity> unitEntityOptional = this.unitRepository.findByName(name);
        if (!unitEntityOptional.isPresent()) {
            throw new NotExistDataException("UnitEntity", name);
        }

        return unitEntityOptional.get().toDTO();
    }

    @Override
    public Unit createUnit(Unit unit) {
        return this.unitRepository.save(unit.toEntity()).toDTO();
    }
}
