package link.myrecipes.api.domain;

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

    public void setRegisterUserId(Integer registerUserId) {
        this.registerUserId = registerUserId;
        this.modifyUserId = registerUserId;
    }

    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
    }
}
