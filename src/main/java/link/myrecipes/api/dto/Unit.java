package link.myrecipes.api.dto;

import link.myrecipes.api.domain.UnitEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Unit {
    private String name;

    private String exchangeUnitName;

    private Double exchangeQuantity;

    @Builder
    public Unit(String name, String exchangeUnitName, Double exchangeQuantity) {
        this.name = name;
        this.exchangeUnitName = exchangeUnitName;
        this.exchangeQuantity = exchangeQuantity;
    }

    public UnitEntity toEntity() {
        return UnitEntity.builder()
                .name(this.getName())
                .exchangeUnitName(this.getExchangeUnitName())
                .exchangeQuantity(this.getExchangeQuantity())
                .build();
    }
}
