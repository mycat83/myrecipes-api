package io.myrecipes.api.dto;

import io.myrecipes.api.domain.UnitEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Unit {
    private String name;

    private String exchangeUnitName;

    private Integer exchangeQuantity;

    private Integer registerUserId;

    private Integer modifyUserId;

    @Builder
    public Unit(String name, String exchangeUnitName, Integer exchangeQuantity, Integer registerUserId, Integer modifyUserId) {
        this.name = name;
        this.exchangeUnitName = exchangeUnitName;
        this.exchangeQuantity = exchangeQuantity;
        this.registerUserId = registerUserId;
        this.modifyUserId = modifyUserId;
    }

    public UnitEntity toEntity() {
        return UnitEntity.builder()
                .name(this.getName())
                .exchangeUnitName(this.getExchangeUnitName())
                .exchangeQuantity(this.getExchangeQuantity())
                .registerUserId(this.getRegisterUserId())
                .modifyUserId(this.getModifyUserId())
                .build();
    }
}
