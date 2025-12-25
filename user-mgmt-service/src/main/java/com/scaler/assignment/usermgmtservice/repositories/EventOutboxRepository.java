package com.scaler.assignment.usermgmtservice.repositories;

import com.scaler.assignment.usermgmtservice.models.EventOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventOutboxRepository extends JpaRepository<EventOutbox, UUID> {

    List<EventOutbox> findByProcessedFalseOrderByCreatedAtAsc();

    @Modifying
    @Query("UPDATE EventOutbox o SET o.processed = true WHERE o.id IN :ids")
    void markAllAsProcessed(@Param("ids") List<UUID> ids);
}
