package link.myrecipes.api.service;

import link.myrecipes.api.domain.MaterialEntity;
import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.Material;
import link.myrecipes.api.dto.request.MaterialRequest;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.repository.MaterialRepository;
import link.myrecipes.api.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final UnitRepository unitRepository;
    private final ModelMapper modelMapper;

    @Override
    public Material readMaterial(int id) {

        Optional<MaterialEntity> materialEntityOptional = this.materialRepository.findById(id);

        if (materialEntityOptional.isEmpty()) {
            throw new NotExistDataException(MaterialEntity.class, id);
        }

        return this.modelMapper.map(materialEntityOptional.get(), Material.class);
    }

    @Override
    public Page<Material> readMaterialList(Pageable pageable) {
        return this.materialRepository.findAll(pageable)
                .map(materialEntity -> modelMapper.map(materialEntity, Material.class));
    }

    @Override
    @Transactional
    public Material createMaterial(MaterialRequest materialRequest, int userId) {

        MaterialEntity materialEntity = this.modelMapper.map(materialRequest, MaterialEntity.class);
        materialEntity.setRegisterUserId(userId);

        Optional<UnitEntity> unitEntityOptional = this.unitRepository.findByName(materialRequest.getUnitName());
        if (unitEntityOptional.isEmpty()) {
            throw new NotExistDataException(UnitEntity.class, materialRequest.getUnitName());
        }

        materialEntity.setUnitEntity(unitEntityOptional.get());
        return this.modelMapper.map(this.materialRepository.save(materialEntity), Material.class);
    }
}
