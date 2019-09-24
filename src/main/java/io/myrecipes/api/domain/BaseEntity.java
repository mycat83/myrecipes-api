package io.myrecipes.api.domain;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
class BaseEntity {
    protected Integer registerUserId;

    @CreationTimestamp
    private LocalDateTime registerDate;

    protected Integer modifyUserId;

    @UpdateTimestamp
    private LocalDateTime modifyDate;
}
