package link.myrecipes.api.repository;

import link.myrecipes.api.domain.RecipeMaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeMaterialRepository extends JpaRepository<RecipeMaterialEntity, Integer> {
}
