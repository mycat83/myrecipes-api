package link.myrecipes.api.repository;

import link.myrecipes.api.domain.RecipeStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeStepRepository extends JpaRepository<RecipeStepEntity, Integer> {
}
