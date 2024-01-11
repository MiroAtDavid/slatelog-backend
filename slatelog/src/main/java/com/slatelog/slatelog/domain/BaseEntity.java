package com.slatelog.slatelog.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
@Getter
public class BaseEntity<PK extends Serializable> {

    @Id protected final PK id;
    @CreatedDate private Instant createdAt;
    @LastModifiedDate private Instant lastModifiedAt;
    @Version private Long version;

    // Constructor
    protected BaseEntity(PK id){
        this.id = id;
    }

    // Methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

}
