package link.myrecipes.api.repository;

import link.myrecipes.api.domain.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer> {
}
