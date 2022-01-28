package com.jtm.contact.core.usecase.repository;

import com.jtm.contact.core.domain.entity.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface MessageRepository extends ReactiveMongoRepository<Message, UUID> {

    Flux<Message> findByClientAddress(String clientAddress);

    Flux<Message> findByDomain(String domain);
}
