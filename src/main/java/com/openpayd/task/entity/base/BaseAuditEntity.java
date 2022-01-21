package com.openpayd.task.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseAuditEntity extends BaseEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedAt;

    @PrePersist
    protected void setCreationParameters() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    protected void setUpdateParameters() {
        this.updatedAt = new Date();
    }
}
