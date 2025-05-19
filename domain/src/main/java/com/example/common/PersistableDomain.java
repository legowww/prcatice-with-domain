package com.example.common;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Getter
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
public abstract class PersistableDomain<ID> {

    protected ID id;
    protected final OffsetDateTime createdAt;
    protected OffsetDateTime updatedAt;
}
