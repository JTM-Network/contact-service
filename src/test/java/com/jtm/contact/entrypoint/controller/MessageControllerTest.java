package com.jtm.contact.entrypoint.controller;

import com.jtm.contact.core.domain.dto.MessageDto;
import com.jtm.contact.core.domain.entity.Message;
import com.jtm.contact.data.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebFluxTest(MessageController.class)
@AutoConfigureWebTestClient
public class MessageControllerTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private MessageService messageService;

    private final Message message = new Message("joe", "matt", "joematt@gmail.com", "Hi mate", "joetymatthews.com", "localhost");
    private final MessageDto dto = new MessageDto("test", "hi", "testhi@gmail.com", "truth", "joetymatthews.com");

    @Test
    public void postMessage() {
        when(messageService.insertMessage(any(), any())).thenReturn(Mono.just(message));

        testClient.post()
                .uri("/message")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("joe")
                .jsonPath("$.lastName").isEqualTo("matt");

        verify(messageService, times(1)).insertMessage(any(), any());
        verifyNoMoreInteractions(messageService);
    }

    @Test
    public void getMessage() {
        when(messageService.getMessage(any())).thenReturn(Mono.just(message));

        testClient.get()
                .uri("/message/" + UUID.randomUUID())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("joe")
                .jsonPath("$.lastName").isEqualTo("matt");

        verify(messageService, times(1)).getMessage(any());
        verifyNoMoreInteractions(messageService);
    }

    @Test
    public void getLatestMessageByClientAddress() {
        when(messageService.getLatestMessageByClientAddress(anyString())).thenReturn(Mono.just(message));

        testClient.get()
                .uri("/message/address?value=test")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("joe")
                .jsonPath("$.lastName").isEqualTo("matt");

        verify(messageService, times(1)).getLatestMessageByClientAddress(anyString());
        verifyNoMoreInteractions(messageService);
    }

    @Test
    public void getMessagesByDomain() {
        when(messageService.getMessagesByDomain(anyString())).thenReturn(Flux.just(message));

        testClient.get()
                .uri("/message/domain?value=test")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].firstName").isEqualTo("joe")
                .jsonPath("$[0].lastName").isEqualTo("matt");

        verify(messageService, times(1)).getMessagesByDomain(anyString());
        verifyNoMoreInteractions(messageService);
    }

    @Test
    public void getMessages() {
        when(messageService.getMessages()).thenReturn(Flux.just(message));

        testClient.get()
                .uri("/message/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].firstName").isEqualTo("joe")
                .jsonPath("$[0].lastName").isEqualTo("matt");

        verify(messageService, times(1)).getMessages();
        verifyNoMoreInteractions(messageService);
    }

    @Test
    public void deleteMessage() {
        when(messageService.deleteMessage(any(UUID.class))).thenReturn(Mono.just(message));

        testClient.delete()
                .uri("/message/" + UUID.randomUUID())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("joe")
                .jsonPath("$.lastName").isEqualTo("matt");

        verify(messageService, times(1)).deleteMessage(any(UUID.class));
        verifyNoMoreInteractions(messageService);
    }
}
