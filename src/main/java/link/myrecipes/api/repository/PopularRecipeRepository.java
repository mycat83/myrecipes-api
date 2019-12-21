package link.myrecipes.api.repository;

import link.myrecipes.api.domain.PopularRecipesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PopularRecipeRepository extends MongoRepository<PopularRecipesDocument, Integer> {
    List<PopularRecipesDocument> findAllByOrderBySequence();
}
