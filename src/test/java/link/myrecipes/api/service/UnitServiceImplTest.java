package link.myrecipes.api.service;

import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.dto.request.UnitRequest;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.repository.UnitRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UnitServiceImplTest {

    private UnitEntity unitEntity;

    @InjectMocks
    private UnitServiceImpl unitService;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void setUp() {

        this.unitEntity = UnitEntity.builder()
                .name("kg")
                .exchangeUnitName("g")
                .exchangeQuantity(1000D)
                .build();
    }

    @Test
    public void When_존재하는_단위_조회_Then_정상_반환() {

        // Given
        Unit unit = makeUnit(this.unitEntity);

        given(this.unitRepository.findByName(this.unitEntity.getName())).willReturn(Optional.ofNullable(this.unitEntity));
        given(this.modelMapper.map(any(UnitEntity.class), eq(Unit.class))).willReturn(unit);

        // When
        final Unit readUnit = this.unitService.readUnit(this.unitEntity.getName());

        // Then
        assertThat(readUnit, instanceOf(Unit.class));
        assertThat(readUnit.getName(), is(this.unitEntity.getName()));
        assertThat(readUnit.getExchangeUnitName(), is(this.unitEntity.getExchangeUnitName()));
        assertThat(readUnit.getExchangeQuantity(), is(this.unitEntity.getExchangeQuantity()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하지_않는_단위_조회_Then_예외_발생() {

        // When
        this.unitService.readUnit(this.unitEntity.getName());
    }

    @Test
    public void When_단위_저장_Then_정상_반환() {

        // Given
        Unit unit = makeUnit(this.unitEntity);
        UnitRequest unitRequest = makeUnitRequest(this.unitEntity);

        given(this.unitRepository.save(any(UnitEntity.class))).willReturn(this.unitEntity);
        given(this.modelMapper.map(any(UnitRequest.class), eq(UnitEntity.class))).willReturn(this.unitEntity);
        given(this.modelMapper.map(any(UnitEntity.class), eq(Unit.class))).willReturn(unit);

        // When
        final Unit savedUnit = this.unitService.createUnit(unitRequest, 10001);

        // Then
        assertThat(savedUnit, instanceOf(Unit.class));
        assertThat(savedUnit.getName(), is(this.unitEntity.getName()));
        assertThat(savedUnit.getExchangeUnitName(), is(this.unitEntity.getExchangeUnitName()));
        assertThat(savedUnit.getExchangeQuantity(), is(this.unitEntity.getExchangeQuantity()));
    }

    private UnitRequest makeUnitRequest(UnitEntity unitEntity) {
        return UnitRequest.builder()
                .name(unitEntity.getName())
                .exchangeUnitName(unitEntity.getExchangeUnitName())
                .exchangeQuantity(unitEntity.getExchangeQuantity())
                .build();
    }

    private Unit makeUnit(UnitEntity unitEntity) {
        return Unit.builder()
                .name(unitEntity.getName())
                .exchangeUnitName(unitEntity.getExchangeUnitName())
                .exchangeQuantity(unitEntity.getExchangeQuantity())
                .build();
    }
}