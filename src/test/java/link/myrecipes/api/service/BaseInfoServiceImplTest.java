package link.myrecipes.api.service;

import link.myrecipes.api.domain.MaterialEntity;
import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.Material;
import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.repository.MaterialRepository;
import link.myrecipes.api.repository.UnitRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class BaseInfoServiceImplTest {
    private MaterialEntity materialEntity;
    private UnitEntity unitEntity;

    @InjectMocks
    private BaseInfoServiceImpl baseInfoService;

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
        //given
        given(this.materialRepository.findById(10)).willReturn(Optional.ofNullable(this.materialEntity));

        //when
        final Material material = this.baseInfoService.readMaterial(10);

        //then
        assertThat(material, instanceOf(Material.class));
        assertThat(material.getName(), is(this.materialEntity.getName()));
        assertThat(material.getUnitName(), is(this.materialEntity.getUnitEntity().getName()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하지_않는_재료_조회_Then_예외_발생() {
        //when
        this.baseInfoService.readMaterial(11);
    }

    @Test
    public void When_재료_리스트_조회_Then_정상_반환() {
        //given
        given(this.materialRepository.findAll()).willReturn(Collections.singletonList(this.materialEntity));

        //when
        final List<Material> materialList = this.baseInfoService.readMaterialList();

        //then
        assertThat(materialList.size(), is(1));
        assertThat(materialList.get(0), instanceOf(Material.class));
        assertThat(materialList.get(0).getName(), is(this.materialEntity.getName()));
        assertThat(materialList.get(0).getUnitName(), is(this.materialEntity.getUnitEntity().getName()));
    }

    @Test
    public void When_존재하는_단위로_재료_저장_Then_정상_반환() {
        //given
        given(this.unitRepository.findByName(this.unitEntity.getName())).willReturn(Optional.ofNullable(this.unitEntity));
        given(this.materialRepository.save(any(MaterialEntity.class))).willReturn(this.materialEntity);

        //when
        final Material material = this.baseInfoService.createMaterial(this.materialEntity.toDTO(), 10001);

        //then
        assertThat(material, instanceOf(Material.class));
        assertThat(material.getName(), is(this.materialEntity.getName()));
        assertThat(material.getUnitName(), is(this.materialEntity.getUnitEntity().getName()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하는_않는_단위로_재료_저장_Then_예외_발생() {
        //when
        this.baseInfoService.createMaterial(this.materialEntity.toDTO(), 10001);
    }

    @Test
    public void When_존재하는_단위_조회_Then_정상_반환() {
        //given
        given(this.unitRepository.findByName(this.unitEntity.getName())).willReturn(Optional.ofNullable(this.unitEntity));

        //when
        final Unit unit = this.baseInfoService.readUnit(this.unitEntity.getName());

        //then
        assertThat(unit, instanceOf(Unit.class));
        assertThat(unit.getName(), is(this.unitEntity.getName()));
        assertThat(unit.getExchangeUnitName(), is(this.unitEntity.getExchangeUnitName()));
        assertThat(unit.getExchangeQuantity(), is(this.unitEntity.getExchangeQuantity()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하지_않는_단위_조회_Then_예외_발생() {
        //when
        this.baseInfoService.readUnit(this.unitEntity.getName());
    }

    @Test
    public void When_단위_저장_Then_정상_반환() {
        //given
        given(this.unitRepository.save(any(UnitEntity.class))).willReturn(unitEntity);

        //when
        final Unit unit = this.baseInfoService.createUnit(this.unitEntity.toDTO(), 10001);

        //then
        assertThat(unit, instanceOf(Unit.class));
        assertThat(unit.getName(), is(this.unitEntity.getName()));
        assertThat(unit.getExchangeUnitName(), is(this.unitEntity.getExchangeUnitName()));
        assertThat(unit.getExchangeQuantity(), is(this.unitEntity.getExchangeQuantity()));
    }
}