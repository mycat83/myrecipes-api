package link.myrecipes.api.repository;

import link.myrecipes.api.domain.PopularRecipeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PopularRecipeRepository extends MongoRepository<PopularRecipeDocument, Integer> {
    List<PopularRecipeDocument> findAllByOrderBySequence();
}
