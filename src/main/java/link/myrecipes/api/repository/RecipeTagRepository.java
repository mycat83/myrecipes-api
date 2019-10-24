package link.myrecipes.api.repository;

import link.myrecipes.api.domain.RecipeTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeTagRepository extends JpaRepository<RecipeTagEntity, Integer> {
}
