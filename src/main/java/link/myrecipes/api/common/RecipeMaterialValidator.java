package link.myrecipes.api.common;

import link.myrecipes.api.domain.MaterialEntity;
import link.myrecipes.api.dto.request.RecipeMaterialRequest;
import link.myrecipes.api.repository.MaterialRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;

@Component
public class RecipeMaterialValidator {

    private final MaterialRepository materialRepository;

    public RecipeMaterialValidator(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public void validate(List<RecipeMaterialRequest> recipeMaterialRequestList, Errors errors) {

        for (RecipeMaterialRequest recipeMaterialRequest : recipeMaterialRequestList) {
            Optional<MaterialEntity> materialEntityOptional = this.materialRepository.findById(recipeMaterialRequest.getMaterialId());
            if (materialEntityOptional.isEmpty()) {
                errors.reject("wrongValue", "재료가 존재하지 않습니다.");
                return;
            }
        }
    }
}
