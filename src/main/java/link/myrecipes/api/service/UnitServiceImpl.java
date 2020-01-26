package link.myrecipes.api.service;

import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.dto.request.UnitRequest;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.repository.UnitRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private ModelMapper modelMapper;

    public UnitServiceImpl(UnitRepository unitRepository, ModelMapper modelMapper) {

        this.unitRepository = unitRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Unit readUnit(String name) {

        Optional<UnitEntity> unitEntityOptional = this.unitRepository.findByName(name);
        if (unitEntityOptional.isEmpty()) {
            throw new NotExistDataException(UnitEntity.class, name);
        }

        return this.modelMapper.map(unitEntityOptional.get(), Unit.class);
    }

    @Override
    @Transactional
    public Unit createUnit(UnitRequest unitRequest, int userId) {

        UnitEntity unitEntity = this.modelMapper.map(unitRequest, UnitEntity.class);
        unitEntity.setRegisterUserId(userId);

        return this.modelMapper.map(this.unitRepository.save(unitEntity), Unit.class);
    }
}
