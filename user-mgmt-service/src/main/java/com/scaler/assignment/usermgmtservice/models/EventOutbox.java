package com.scaler.assignment.usermgmtservice.models;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "aggregate_type") private String aggregateType;
    @Column(name = "aggregate_id") private UUID aggregateId;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "processed") private Boolean processed;
    private String type;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private String payload;
}

