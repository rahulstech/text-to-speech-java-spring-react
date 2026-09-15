package com.github.rahulstech.tts.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "jobs")
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @ColumnDefault("CREATED")
    @Enumerated(EnumType.STRING)
    private Status status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> properties;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,Object> result;

    public enum Status {
        CREATED,

        RUNNING,

        SUCCESSFUL,

        FAIL,

        CANCELED
    }

    public final boolean isFinished() {
        return  status == Status.SUCCESSFUL
                || status == Status.FAIL
                || status == Status.CANCELED;
    }

    public final boolean isCanceled() { return status == Status.CANCELED; }
}
