package com.jtm.contact.entrypoint.controller;

import com.jtm.contact.core.domain.dto.MessageDto;
import com.jtm.contact.core.domain.entity.Message;
import com.jtm.contact.data.service.MessageService;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public Mono<Message> postMessage(ServerHttpRequest request, @RequestBody MessageDto dto) {
        return messageService.insertMessage(request, dto);
    }

    @GetMapping("/{id}")
    public Mono<Message> getMessage(@PathVariable UUID id) {
        return messageService.getMessage(id);
    }

    @GetMapping("/address")
    public Mono<Message> getLatestMessageByAddress(@RequestParam("value") String address) {
        return messageService.getLatestMessageByClientAddress(address);
    }

    @GetMapping("/domain")
    public Flux<Message> getMessagesByDomain(@RequestParam("value") String domain) {
        return messageService.getMessagesByDomain(domain);
    }

    @GetMapping("/all")
    public Flux<Message> getMessages() {
        return messageService.getMessages();
    }

    @DeleteMapping("/{id}")
    public Mono<Message> deleteMessage(@PathVariable UUID id) {
        return messageService.deleteMessage(id);
    }
}
