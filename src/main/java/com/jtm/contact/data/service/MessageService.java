package com.jtm.contact.data.service;

import com.jtm.contact.core.domain.dto.MessageDto;
import com.jtm.contact.core.domain.entity.Message;
import com.jtm.contact.core.domain.exceptions.FailedRemoteAddress;
import com.jtm.contact.core.domain.exceptions.MessageLimitReached;
import com.jtm.contact.core.domain.exceptions.MessageNotFound;
import com.jtm.contact.core.usecase.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Comparator;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Mono<Message> insertMessage(ServerHttpRequest request, MessageDto dto) {
        if (!request.getHeaders().containsKey("CLIENT_ADDRESS")) return Mono.error(FailedRemoteAddress::new);
        String address = request.getHeaders().getFirst("CLIENT_ADDRESS");
        return messageRepository.findByClientAddress(address)
                .sort(Comparator.comparingLong(Message::getSentTime).reversed())
                .next()
                .flatMap(msg -> {
                    if (!msg.canMessage()) return Mono.error(MessageLimitReached::new);
                    return messageRepository.save(new Message(dto, address));
                })
                .switchIfEmpty(Mono.defer(() -> messageRepository.save(new Message(dto, address))));
    }

    public Mono<Message> getMessage(UUID id) {
        return messageRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> Mono.error(MessageNotFound::new)));
    }

    public Mono<Message> getLatestMessageByClientAddress(String clientAddress) {
        return messageRepository.findByClientAddress(clientAddress)
                .sort(Comparator.comparing(Message::getSentTime).reversed())
                .next();
    }

    public Flux<Message> getMessagesByDomain(String domain) {
        return messageRepository.findByDomain(domain);
    }

    public Flux<Message> getMessages() {
        return messageRepository.findAll();
    }

    public Mono<Message> deleteMessage(UUID id) {
        return messageRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> Mono.error(MessageNotFound::new)))
                .flatMap(next -> messageRepository.delete(next).thenReturn(next));
    }
}
