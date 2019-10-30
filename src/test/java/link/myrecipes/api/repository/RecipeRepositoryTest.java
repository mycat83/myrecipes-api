package link.myrecipes.api.repository;

import link.myrecipes.api.domain.*;
import link.myrecipes.api.exception.NotExistDataException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RecipeRepositoryTest {
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

    private RecipeEntity recipeEntity1;
    private RecipeEntity recipeEntity2;
    private RecipeEntity recipeEntity3;

    @Before
    public void setUp() {
        this.recipeEntity1 = RecipeEntity.builder().title("test1").image("image1.jpg").estimatedTime(30).difficulty(1).registerUserId(1001).build();
        this.recipeEntity2 = RecipeEntity.builder().title("test2").image("image2.jpg").estimatedTime(60).difficulty(3).registerUserId(1002).build();
        this.recipeEntity3 = RecipeEntity.builder().title("test3").image("image3.jpg").estimatedTime(90).difficulty(5).registerUserId(1003).build();
    }

    @Test
    public void Should_동일한_엔티티_반환_When_엔티티_저장() {
        this.recipeRepository.save(this.recipeEntity1);

        Optional<RecipeEntity> recipeEntityOptional1 = this.recipeRepository.findById(this.recipeEntity1.getId());
        if (!recipeEntityOptional1.isPresent()) {
            throw new NotExistDataException(RecipeEntity.class, this.recipeEntity1.getId());
        }
        final RecipeEntity savedRecipeEntity1 = recipeEntityOptional1.get();

        assertThat(savedRecipeEntity1.getTitle(), is(this.recipeEntity1.getTitle()));
        assertThat(savedRecipeEntity1.getImage(), is(this.recipeEntity1.getImage()));
        assertThat(savedRecipeEntity1.getEstimatedTime(), is(this.recipeEntity1.getEstimatedTime()));
        assertThat(savedRecipeEntity1.getDifficulty(), is(this.recipeEntity1.getDifficulty()));
        assertThat(savedRecipeEntity1.getRegisterUserId(), is(this.recipeEntity1.getRegisterUserId()));

        this.recipeEntity1.update(this.recipeEntity2);
        this.recipeRepository.save(this.recipeEntity1);

        Optional<RecipeEntity> recipeEntityOptional2 = this.recipeRepository.findById(this.recipeEntity1.getId());
        if (!recipeEntityOptional2.isPresent()) {
            throw new NotExistDataException(RecipeEntity.class, this.recipeEntity1.getId());
        }
        final RecipeEntity savedRecipeEntity2 = recipeEntityOptional2.get();

        assertThat(savedRecipeEntity2.getTitle(), is(this.recipeEntity2.getTitle()));
        assertThat(savedRecipeEntity2.getImage(), is(this.recipeEntity2.getImage()));
        assertThat(savedRecipeEntity2.getEstimatedTime(), is(this.recipeEntity2.getEstimatedTime()));
        assertThat(savedRecipeEntity2.getDifficulty(), is(this.recipeEntity2.getDifficulty()));
        assertThat(savedRecipeEntity2.getModifyUserId(), is(this.recipeEntity2.getModifyUserId()));
    }

    @Test(expected = NotExistDataException.class)
    public void Should_없는_ID로_조회시_예외발생_When_엔티티_저장() {
        this.recipeRepository.save(this.recipeEntity1);

        Optional<RecipeEntity> recipeEntityOptional = this.recipeRepository.findById(0);
        if (!recipeEntityOptional.isPresent()) {
            throw new NotExistDataException(RecipeEntity.class, 0);
        }
        final RecipeEntity savedRecipeEntity = recipeEntityOptional.get();
    }

    @Test
    public void Should_키_순차적_증가_When_엔티티_여러개_저장() {
        recipeRepository.save(this.recipeEntity1);
        recipeRepository.save(this.recipeEntity2);
        recipeRepository.save(this.recipeEntity3);
        final List<RecipeEntity> recipeEntityList = this.recipeRepository.findAll();

        assertThat(recipeEntityList.size(), is(3));
        assertThat(recipeEntityList.get(0).getId(), is(1));
        assertThat(recipeEntityList.get(1).getId(), is(2));
        assertThat(recipeEntityList.get(2).getId(), is(3));
    }

    @Test
    public void Should_엔티티_없음_When_엔티티_저장후_삭제() {
        this.recipeRepository.save(this.recipeEntity1);
        this.recipeRepository.deleteById(this.recipeEntity1.getId());
        final Optional<RecipeEntity> recipeOptional = this.recipeRepository.findById(this.recipeEntity1.getId());

        assertThat(recipeOptional.isPresent(), is(false));
    }

    @Test
    public void Should_연관관계_정상_조회_When_연관관계_매핑() {
        this.recipeRepository.save(this.recipeEntity1);

        List<UnitEntity> unitEntityList = new ArrayList<>();
        unitEntityList.add(UnitEntity.builder().name("g").build());
        unitEntityList.add(UnitEntity.builder().name("kg").exchangeUnitName("g").registerUserId(1000).build());
        this.unitRepository.saveAll(unitEntityList);

        List<MaterialEntity> materialEntityList = new ArrayList<>();
        materialEntityList.add(MaterialEntity.builder().name("material1").registerUserId(1001).unitEntity(unitEntityList.get(0)).build());
        materialEntityList.add(MaterialEntity.builder().name("material2").registerUserId(1002).unitEntity(unitEntityList.get(1)).build());
        materialEntityList.add(MaterialEntity.builder().name("material3").registerUserId(1003).unitEntity(unitEntityList.get(0)).build());
        this.materialRepository.saveAll(materialEntityList);

        List<RecipeMaterialEntity> recipeMaterialEntityList = new ArrayList<>();
        recipeMaterialEntityList.add(RecipeMaterialEntity.builder().quantity(10D).recipeEntity(this.recipeEntity1).materialEntity(materialEntityList.get(0)).build());
        recipeMaterialEntityList.add(RecipeMaterialEntity.builder().quantity(20D).recipeEntity(this.recipeEntity1).materialEntity(materialEntityList.get(1)).build());
        recipeMaterialEntityList.add(RecipeMaterialEntity.builder().quantity(30D).recipeEntity(this.recipeEntity1).materialEntity(materialEntityList.get(2)).build());
        recipeMaterialEntityList.get(0).getRecipeEntity().getRecipeMaterialEntityList().add(recipeMaterialEntityList.get(0));
        recipeMaterialEntityList.get(1).getRecipeEntity().getRecipeMaterialEntityList().add(recipeMaterialEntityList.get(1));
        recipeMaterialEntityList.get(2).getRecipeEntity().getRecipeMaterialEntityList().add(recipeMaterialEntityList.get(2));
        this.recipeMaterialRepository.saveAll(recipeMaterialEntityList);

        List<RecipeStepEntity> recipeStepEntityList = new ArrayList<>();
        recipeStepEntityList.add(RecipeStepEntity.builder().step(1).content("step1").image("step1.jpg").recipeEntity(this.recipeEntity1).build());
        recipeStepEntityList.add(RecipeStepEntity.builder().step(1).content("step2").image("step2.jpg").recipeEntity(this.recipeEntity1).build());
        recipeStepEntityList.get(0).getRecipeEntity().getRecipeStepEntityList().add(recipeStepEntityList.get(0));
        recipeStepEntityList.get(1).getRecipeEntity().getRecipeStepEntityList().add(recipeStepEntityList.get(1));
        this.recipeStepRepository.saveAll(recipeStepEntityList);

        RecipeTagEntity recipeTagEntity = RecipeTagEntity.builder().tag("tag1").recipeEntity(this.recipeEntity1).build();
        recipeTagEntity.getRecipeEntity().getRecipeTagEntityList().add(recipeTagEntity);
        this.recipeTagRepository.save(recipeTagEntity);

        final RecipeEntity savedRecipeEntity = this.recipeRepository.getOne(this.recipeEntity1.getId());

        assertThat(savedRecipeEntity.getRecipeMaterialEntityList().size(), is(3));
        assertThat(savedRecipeEntity.getRecipeMaterialEntityList().get(0).getQuantity(), is(10D));
        assertThat(savedRecipeEntity.getRecipeMaterialEntityList().get(1).getQuantity(), is(20D));
        assertThat(savedRecipeEntity.getRecipeMaterialEntityList().get(2).getQuantity(), is(30D));

        assertThat(savedRecipeEntity.getRecipeMaterialEntityList().get(0).getMaterialEntity().getName(), is("material1"));
        assertThat(savedRecipeEntity.getRecipeMaterialEntityList().get(1).getMaterialEntity().getName(), is("material2"));
        assertThat(savedRecipeEntity.getRecipeMaterialEntityList().get(2).getMaterialEntity().getName(), is("material3"));

        assertThat(savedRecipeEntity.getRecipeMaterialEntityList().get(0).getMaterialEntity().getUnitEntity().getName(), is("g"));
        assertThat(savedRecipeEntity.getRecipeMaterialEntityList().get(1).getMaterialEntity().getUnitEntity().getName(), is("kg"));
        assertThat(savedRecipeEntity.getRecipeMaterialEntityList().get(2).getMaterialEntity().getUnitEntity().getName(), is("g"));

        assertThat(savedRecipeEntity.getRecipeStepEntityList().size(), is(2));
        assertThat(savedRecipeEntity.getRecipeStepEntityList().get(0).getContent(), is("step1"));
        assertThat(savedRecipeEntity.getRecipeStepEntityList().get(1).getContent(), is("step2"));

        assertThat(savedRecipeEntity.getRecipeTagEntityList().size(), is(1));
        assertThat(savedRecipeEntity.getRecipeTagEntityList().get(0).getTag(), is("tag1"));
    }

    @Test
    public void Should_카운트_1_반환_When_1건_저장() {
        this.recipeRepository.save(this.recipeEntity1);
        final long recipeCnt = this.recipeRepository.count();

        assertThat(recipeCnt, is(1L));
    }
}