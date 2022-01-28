package com.jtm.contact.data.service;

import com.jtm.contact.core.domain.dto.MessageDto;
import com.jtm.contact.core.domain.entity.Message;
import com.jtm.contact.core.domain.exceptions.MessageLimitReached;
import com.jtm.contact.core.domain.exceptions.MessageNotFound;
import com.jtm.contact.core.usecase.repository.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class MessageServiceTest {

    private final MessageRepository messageRepository = mock(MessageRepository.class);
    private final MessageService messageService = new MessageService(messageRepository);
    private final Message message = new Message("joe", "matt", "joematt@gmail.com", "Hi mate", "joetymatthews.com", "localhost");
    private final MessageDto dto = new MessageDto("test", "hi", "testhi@gmail.com", "truth");
    private final ServerHttpRequest request = mock(ServerHttpRequest.class);

    @Before
    public void setup() {
        InetSocketAddress socketAddress = mock(InetSocketAddress.class);
        InetAddress inetAddress = mock(InetAddress.class);

        when(request.getRemoteAddress()).thenReturn(socketAddress);
        when(socketAddress.getAddress()).thenReturn(inetAddress);
        when(inetAddress.getHostAddress()).thenReturn("localhost");
    }

    @Test
    public void insertMessage_thenLimitReached() {
        when(messageRepository.findByClientAddress(anyString())).thenReturn(Flux.just(message));

        Mono<Message> returned = messageService.insertMessage(request, dto);

        verify(messageRepository, times(1)).findByClientAddress(anyString());
        verifyNoMoreInteractions(messageRepository);

        StepVerifier.create(returned)
                .expectError(MessageLimitReached.class)
                .verify();
    }

    @Test
    public void insertMessage() {
        when(messageRepository.findByClientAddress(anyString())).thenReturn(Flux.just(message, new Message("jamie", "jenkins", "jamijenkins@gmail.com", "Test", "joetymatthews.com", "localhost", (System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2)))));
        when(messageRepository.save(any())).thenReturn(Mono.just(message));

        Mono<Message> returned = messageService.insertMessage(request, dto);

        verify(messageRepository, times(1)).findByClientAddress(anyString());
        verifyNoMoreInteractions(messageRepository);

        StepVerifier.create(returned)
                .assertNext(next -> {
                    assertFalse(next.canMessage());
                    assertThat(next.getClientAddress()).isEqualTo("localhost");
                })
                .verifyComplete();
    }

    @Test
    public void getMessage_theNotFound() {
        when(messageRepository.findById(any(UUID.class))).thenReturn(Mono.empty());

        Mono<Message> returned = messageService.getMessage(UUID.randomUUID());

        verify(messageRepository, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(messageRepository);

        StepVerifier.create(returned)
                .expectError(MessageNotFound.class)
                .verify();
    }

    @Test
    public void getMessage() {
        when(messageRepository.findById(any(UUID.class))).thenReturn(Mono.just(message));

        Mono<Message> returned = messageService.getMessage(UUID.randomUUID());

        verify(messageRepository, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(messageRepository);

        StepVerifier.create(returned)
                .assertNext(next -> {
                    assertThat(next.getFirstName()).isEqualTo(message.getFirstName());
                    assertThat(next.getLastName()).isEqualTo(message.getLastName());
                    assertThat(next.getDomain()).isEqualTo("joetymatthews.com");
                })
                .verifyComplete();
    }

    @Test
    public void getMessagesByDomain() {
        when(messageRepository.findByDomain(anyString())).thenReturn(Flux.just(message));

        Flux<Message> returned = messageService.getMessagesByDomain("test");

        verify(messageRepository, times(1)).findByDomain(anyString());
        verifyNoMoreInteractions(messageRepository);

        StepVerifier.create(returned)
                .assertNext(next -> {

                })
                .verifyComplete();
    }

    @Test
    public void getMessages() {}

    @Test
    public void deleteMessage_thenNotFound() {}

    @Test
    public void deleteMessage() {}
}
