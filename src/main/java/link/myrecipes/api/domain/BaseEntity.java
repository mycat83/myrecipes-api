package link.myrecipes.api.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
class BaseEntity {

    // TODO: JWT 인증 적용 후 수정
    //    @CreatedBy
    protected Integer registerUserId;

    @CreatedDate
    private LocalDateTime registerDate;

    // TODO: JWT 인증 적용 후 수정
    //    @LastModifiedBy
    protected Integer modifyUserId;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    public void setRegisterUserId(Integer registerUserId) {
        this.registerUserId = registerUserId;
        this.modifyUserId = registerUserId;
    }

    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
    }
}
