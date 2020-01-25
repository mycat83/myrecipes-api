package link.myrecipes.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
}
