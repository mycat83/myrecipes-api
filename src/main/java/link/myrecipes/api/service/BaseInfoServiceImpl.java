package link.myrecipes.api.service;

import link.myrecipes.api.domain.MaterialEntity;
import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.Material;
import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.exception.DuplicateDataException;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.repository.MaterialRepository;
import link.myrecipes.api.repository.UnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            throw new NotExistDataException(MaterialEntity.class, id);
        }

        return materialEntityOptional.get().toDTO();
    }

    @Override
    public List<Material> readMaterialList() {
        return this.materialRepository.findAll()
                .stream()
                .map(MaterialEntity::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Material createMaterial(Material material, int userId) {
        MaterialEntity materialEntity = material.toEntity();
        materialEntity.setRegisterUserId(userId);

        Optional<UnitEntity> unitEntityOptional = this.unitRepository.findByName(material.getUnitName());
        if (!unitEntityOptional.isPresent()) {
            throw new NotExistDataException(UnitEntity.class, material.getUnitName());
        }

        materialEntity.setUnitEntity(unitEntityOptional.get());
        return this.materialRepository.save(materialEntity).toDTO();
    }

    @Override
    public Unit readUnit(String name) {
        Optional<UnitEntity> unitEntityOptional = this.unitRepository.findByName(name);
        if (!unitEntityOptional.isPresent()) {
            throw new NotExistDataException(UnitEntity.class, name);
        }

        return unitEntityOptional.get().toDTO();
    }

    @Override
    @Transactional
    public Unit createUnit(Unit unit, int userId) {
        UnitEntity unitEntity = unit.toEntity();
        unitEntity.setRegisterUserId(userId);

        Optional<UnitEntity> unitEntityOptional = this.unitRepository.findByName(unitEntity.getName());
        if (unitEntityOptional.isPresent()) {
            throw new DuplicateDataException(UnitEntity.class, unitEntity.getName());
        }

        return this.unitRepository.save(unitEntity).toDTO();
    }
}
