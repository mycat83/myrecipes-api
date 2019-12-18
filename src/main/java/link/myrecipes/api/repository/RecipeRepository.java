package link.myrecipes.api.repository;

import link.myrecipes.api.domain.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer> {
    @Modifying
    @Query("update RecipeEntity r set r.readCount = r.readCount + 1 where r.id = :id")
    void increaseReadCount(@Param("id") int id);
}
