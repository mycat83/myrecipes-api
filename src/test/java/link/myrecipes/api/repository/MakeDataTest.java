package link.myrecipes.api.repository;

import link.myrecipes.api.domain.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@Ignore
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MakeDataTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeMaterialRepository recipeMaterialRepository;

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @Autowired
    private RecipeTagRepository recipeTagRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UnitRepository unitRepository;

    private List<RecipeEntity> recipeEntityList = new ArrayList<>();
    private List<UnitEntity> unitEntityList = new ArrayList<>();
    private List<MaterialEntity> materialEntityList = new ArrayList<>();

    @Before
    public void setUp() {
        this.recipeEntityList.add(RecipeEntity.builder().title("test1").image("image1.jpg").estimatedTime(30).difficulty(1).registerUserId(1001).build());
        this.recipeEntityList.add(RecipeEntity.builder().title("test2").image("image2.jpg").estimatedTime(60).difficulty(3).registerUserId(1002).build());
        this.recipeEntityList.add(RecipeEntity.builder().title("test3").image("image3.jpg").estimatedTime(90).difficulty(5).registerUserId(1003).build());

        this.unitEntityList.add(UnitEntity.builder().name("g").build());
        this.unitEntityList.add(UnitEntity.builder().name("kg").exchangeUnitName("g").registerUserId(1000).build());

        this.materialEntityList.add(MaterialEntity.builder().name("material1").registerUserId(1001).unitEntity(unitEntityList.get(0)).build());
        this.materialEntityList.add(MaterialEntity.builder().name("material2").registerUserId(1002).unitEntity(unitEntityList.get(1)).build());
        this.materialEntityList.add(MaterialEntity.builder().name("material3").registerUserId(1003).unitEntity(unitEntityList.get(0)).build());
    }

    @Test
//    @Rollback(false)
    public void 레시피데이터_생성() {
        for (RecipeEntity recipeEntity : recipeEntityList) {
            makeData(recipeEntity);
        }
    }

    public void makeData(RecipeEntity recipeEntity) {
        this.recipeRepository.save(recipeEntity);
        this.unitRepository.saveAll(this.unitEntityList);
        this.materialRepository.saveAll(this.materialEntityList);

        List<RecipeMaterialEntity> recipeMaterialEntityList = new ArrayList<>();
        recipeMaterialEntityList.add(RecipeMaterialEntity.builder().quantity(10D).recipeEntity(recipeEntity).materialEntity(this.materialEntityList.get(0)).build());
        recipeMaterialEntityList.add(RecipeMaterialEntity.builder().quantity(20D).recipeEntity(recipeEntity).materialEntity(this.materialEntityList.get(1)).build());
        recipeMaterialEntityList.add(RecipeMaterialEntity.builder().quantity(30D).recipeEntity(recipeEntity).materialEntity(this.materialEntityList.get(2)).build());
        recipeMaterialEntityList.get(0).getRecipeEntity().getRecipeMaterialEntityList().add(recipeMaterialEntityList.get(0));
        recipeMaterialEntityList.get(1).getRecipeEntity().getRecipeMaterialEntityList().add(recipeMaterialEntityList.get(1));
        recipeMaterialEntityList.get(2).getRecipeEntity().getRecipeMaterialEntityList().add(recipeMaterialEntityList.get(2));
        this.recipeMaterialRepository.saveAll(recipeMaterialEntityList);

        List<RecipeStepEntity> recipeStepEntityList = new ArrayList<>();
        recipeStepEntityList.add(RecipeStepEntity.builder().step(1).content("step1").image("step1.jpg").recipeEntity(recipeEntity).build());
        recipeStepEntityList.add(RecipeStepEntity.builder().step(1).content("step2").image("step2.jpg").recipeEntity(recipeEntity).build());
        recipeStepEntityList.get(0).getRecipeEntity().getRecipeStepEntityList().add(recipeStepEntityList.get(0));
        recipeStepEntityList.get(1).getRecipeEntity().getRecipeStepEntityList().add(recipeStepEntityList.get(1));
        this.recipeStepRepository.saveAll(recipeStepEntityList);

        RecipeTagEntity recipeTagEntity1 = RecipeTagEntity.builder().tag("tag1").recipeEntity(recipeEntity).build();
        RecipeTagEntity recipeTagEntity2 = RecipeTagEntity.builder().tag("tag2").recipeEntity(recipeEntity).build();
        recipeTagEntity1.getRecipeEntity().getRecipeTagEntityList().add(recipeTagEntity1);
        recipeTagEntity2.getRecipeEntity().getRecipeTagEntityList().add(recipeTagEntity2);
        this.recipeTagRepository.save(recipeTagEntity1);
        this.recipeTagRepository.save(recipeTagEntity2);
    }
}
