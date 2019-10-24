package link.myrecipes.api.repository;

import link.myrecipes.api.domain.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<MaterialEntity, Integer> {
}
