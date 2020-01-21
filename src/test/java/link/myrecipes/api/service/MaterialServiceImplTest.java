package link.myrecipes.api.service;

import link.myrecipes.api.domain.MaterialEntity;
import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.Material;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.repository.MaterialRepository;
import link.myrecipes.api.repository.UnitRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class MaterialServiceImplTest {

    private MaterialEntity materialEntity;
    private UnitEntity unitEntity;

    @InjectMocks
    private MaterialServiceImpl materialService;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private UnitRepository unitRepository;

    @Before
    public void setUp() {

        this.unitEntity = UnitEntity.builder()
                .name("kg")
                .exchangeUnitName("g")
                .exchangeQuantity(1000D)
                .build();

        this.materialEntity = MaterialEntity.builder()
                .name("재료")
                .unitEntity(this.unitEntity)
                .build();
    }

    @Test
    public void When_존재하는_재료_조회_Then_정상_반환() {

        // Given
        given(this.materialRepository.findById(10)).willReturn(Optional.ofNullable(this.materialEntity));

        // When
        final Material material = this.materialService.readMaterial(10);

        // Then
        assertThat(material, instanceOf(Material.class));
        assertThat(material.getName(), is(this.materialEntity.getName()));
        assertThat(material.getUnitName(), is(this.materialEntity.getUnitEntity().getName()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하지_않는_재료_조회_Then_예외_발생() {

        // When
        this.materialService.readMaterial(11);
    }

    @Test
    public void When_재료_리스트_조회_Then_정상_반환() {

        // Given
        given(this.materialRepository.findAll()).willReturn(Collections.singletonList(this.materialEntity));

        // When
        final Page<Material> materialPage = this.materialService.readMaterialList(PageRequest.of(10, 0));

        // Then
        assertThat(materialPage.getSize(), is(1));
        assertThat(materialPage.getContent().get(0), instanceOf(Material.class));
        assertThat(materialPage.getContent().get(0).getName(), is(this.materialEntity.getName()));
        assertThat(materialPage.getContent().get(0).getUnitName(), is(this.materialEntity.getUnitEntity().getName()));
    }

    @Test
    public void When_존재하는_단위로_재료_저장_Then_정상_반환() {

        // Given
        given(this.unitRepository.findByName(this.unitEntity.getName())).willReturn(Optional.ofNullable(this.unitEntity));
        given(this.materialRepository.save(any(MaterialEntity.class))).willReturn(this.materialEntity);

        // When
        final Material material = this.materialService.createMaterial(this.materialEntity.toDTO(), 10001);

        // Then
        assertThat(material, instanceOf(Material.class));
        assertThat(material.getName(), is(this.materialEntity.getName()));
        assertThat(material.getUnitName(), is(this.materialEntity.getUnitEntity().getName()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하는_않는_단위로_재료_저장_Then_예외_발생() {

        // When
        this.materialService.createMaterial(this.materialEntity.toDTO(), 10001);
    }
}