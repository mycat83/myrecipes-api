package io.myrecipes.api.repository;

import io.myrecipes.api.domain.RecipeMaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeMaterialRepository extends JpaRepository<RecipeMaterialEntity, Integer> {
}
