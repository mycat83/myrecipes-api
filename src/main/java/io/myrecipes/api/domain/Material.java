package io.myrecipes.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "material")
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Material {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private Integer registerUserId;

    @CreationTimestamp
    private Timestamp registerDate;

    private Integer modifyUserId;

    @UpdateTimestamp
    private Timestamp modifyDate;

    @ManyToOne
    @JoinColumn(name = "unit_name")
    private Unit unit;

    public Material(String name, Integer registerUserId, Unit unit) {
        this.name = name;
        this.unit = unit;
        this.registerUserId = registerUserId;
    }
}
