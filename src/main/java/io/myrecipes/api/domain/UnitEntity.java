package io.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.myrecipes.api.dto.Unit;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "unit")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@JsonIgnoreProperties("hibernateLazyInitializer")
public class UnitEntity extends BaseEntity {
    @Id
    private String name;

    private String exchangeUnitName;

    private Integer exchangeQuantity;

    @Builder
    public UnitEntity(String name, String exchangeUnitName, Integer exchangeQuantity, Integer registerUserId, Integer modifyUserId) {
        this.name = name;
        this.exchangeUnitName = exchangeUnitName;
        this.exchangeQuantity = exchangeQuantity;
        this.registerUserId = registerUserId;
        this.modifyUserId = modifyUserId;
    }

    public Unit toDTO() {
        return Unit.builder()
                .name(this.getName())
                .exchangeUnitName(this.getExchangeUnitName())
                .exchangeQuantity(this.getExchangeQuantity())
                .build();
    }
}
