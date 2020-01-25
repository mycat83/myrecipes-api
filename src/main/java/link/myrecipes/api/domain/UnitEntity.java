package link.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "unit")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@JsonIgnoreProperties("hibernateLazyInitializer")
public class UnitEntity extends BaseEntity {
    @Id
    private String name;

    private String exchangeUnitName;

    private Double exchangeQuantity;

    @Builder
    public UnitEntity(String name, String exchangeUnitName, Double exchangeQuantity, Integer registerUserId, Integer modifyUserId) {
        this.name = name;
        this.exchangeUnitName = exchangeUnitName;
        this.exchangeQuantity = exchangeQuantity;
        this.registerUserId = registerUserId;
        this.modifyUserId = modifyUserId;
    }
}
