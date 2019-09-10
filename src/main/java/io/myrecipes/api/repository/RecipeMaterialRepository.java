package io.myrecipes.api.repository;

import io.myrecipes.api.domain.Recipe;
import io.myrecipes.api.domain.RecipeMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeMaterialRepository extends JpaRepository<RecipeMaterial, Integer> {
}
