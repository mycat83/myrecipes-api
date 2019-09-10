package io.myrecipes.api.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "unit")
public class Unit {
    @Id
    private String name;

    private String exchangeUnitName;

    private Integer exchangeQuantity;

    private Integer registerUserId;

    @CreationTimestamp
    private Timestamp registerDate;

    private Integer modifyUserId;

    @UpdateTimestamp
    private Timestamp modifyDate;

    public Unit(String name) {
        this.name = name;
    }

    public Unit(String name, String exchangeUnitName, Integer exchangeQuantity) {
        this.name = name;
        this.exchangeUnitName = exchangeUnitName;
        this.exchangeQuantity = exchangeQuantity;
    }
}
