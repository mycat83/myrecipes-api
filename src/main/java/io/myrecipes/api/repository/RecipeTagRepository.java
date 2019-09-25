package io.myrecipes.api.repository;

import io.myrecipes.api.domain.RecipeTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeTagRepository extends JpaRepository<RecipeTagEntity, Integer> {
}
