package link.myrecipes.api.service;

import link.myrecipes.api.domain.MaterialEntity;
import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.Material;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.repository.MaterialRepository;
import link.myrecipes.api.repository.UnitRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final UnitRepository unitRepository;
    private final ModelMapper modelMapper;

    public MaterialServiceImpl(MaterialRepository materialRepository, UnitRepository unitRepository, ModelMapper modelMapper) {

        this.materialRepository = materialRepository;
        this.unitRepository = unitRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Material readMaterial(int id) {

        Optional<MaterialEntity> materialEntityOptional = this.materialRepository.findById(id);

        if (materialEntityOptional.isEmpty()) {
            throw new NotExistDataException(MaterialEntity.class, id);
        }

        return materialEntityOptional.get().toDTO();
    }

    @Override
    public Page<Material> readMaterialList(Pageable pageable) {
        return this.materialRepository.findAll(pageable)
                .map(materialEntity -> modelMapper.map(materialEntity, Material.class));
    }

    @Override
    @Transactional
    public Material createMaterial(Material material, int userId) {

        MaterialEntity materialEntity = material.toEntity();
        materialEntity.setRegisterUserId(userId);

        Optional<UnitEntity> unitEntityOptional = this.unitRepository.findByName(material.getUnitName());
        if (unitEntityOptional.isEmpty()) {
            throw new NotExistDataException(UnitEntity.class, material.getUnitName());
        }

        materialEntity.setUnitEntity(unitEntityOptional.get());
        return this.materialRepository.save(materialEntity).toDTO();
    }
}
