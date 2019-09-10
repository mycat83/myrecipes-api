package io.myrecipes.api.repository;

import io.myrecipes.api.domain.Recipe;
import io.myrecipes.api.domain.RecipeTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeTagRepository extends JpaRepository<RecipeTag, Integer> {
}
