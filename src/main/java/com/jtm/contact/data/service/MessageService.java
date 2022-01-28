package com.jtm.contact.data.service;

import com.jtm.contact.core.domain.dto.MessageDto;
import com.jtm.contact.core.domain.entity.Message;
import com.jtm.contact.core.usecase.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Mono<Message> insertMessage(ServerHttpRequest request, MessageDto dto) {
        return Mono.empty();
    }

    public Mono<Message> getMessage(UUID id) {
        return Mono.empty();
    }

    public Mono<Message> getLatestMessageByClientAddress() {
        return Mono.empty();
    }

    public Flux<Message> getMessagesByDomain(String domain) {
        return Flux.empty();
    }

    public Flux<Message> getMessages() {
        return Flux.empty();
    }

    public Mono<Message> deleteMessage(UUID id) {
        return Mono.empty();
    }
}
