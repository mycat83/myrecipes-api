package link.myrecipes.api.common;

import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.request.MaterialRequest;
import link.myrecipes.api.repository.UnitRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Optional;

@Component
public class MaterialValidator {

    private final UnitRepository unitRepository;

    public MaterialValidator(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public void validate(MaterialRequest materialRequest, Errors errors) {

        Optional<UnitEntity> unitEntityOptional = this.unitRepository.findByName(materialRequest.getUnitName());
        if (unitEntityOptional.isEmpty()) {
            errors.rejectValue("unitName", "wrongValue", "재료 단위가 존재하지 않습니다.");
        }
    }
}
