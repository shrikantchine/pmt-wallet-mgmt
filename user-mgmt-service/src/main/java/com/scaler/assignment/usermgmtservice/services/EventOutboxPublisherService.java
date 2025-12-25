package com.scaler.assignment.usermgmtservice.services;

import com.scaler.assignment.usermgmtservice.models.EventOutbox;
import com.scaler.assignment.usermgmtservice.repositories.EventOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class EventOutboxPublisherService {

    private final EventOutboxRepository eventOutboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 500000, initialDelay = 1000)
    @Transactional
    public void eventOutboxPublish() {
        List<EventOutbox> events = eventOutboxRepository.findByProcessedFalseOrderByCreatedAtAsc();
        if (events.isEmpty()) {
            return;
        }
        List<UUID> successfulIds = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (EventOutbox event : events) {
            var topic = event.getAggregateType().toLowerCase() + "_events";
            String key = event.getAggregateId().toString();
            var future = kafkaTemplate.send(topic, key, event.getPayload())
                    .toCompletableFuture()
                    .thenAccept(result -> successfulIds.add(event.getAggregateId()))
                    .exceptionally(ex -> {
                        log.error("Failed to relay event {}: {}", event.getId(), ex.getMessage());
                        return null;
                    });
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        if (!successfulIds.isEmpty()) {
            eventOutboxRepository.markAllAsProcessed(successfulIds);
            log.info("Batch processed: {} messages succeeded", successfulIds.size());
        }
    }
}
