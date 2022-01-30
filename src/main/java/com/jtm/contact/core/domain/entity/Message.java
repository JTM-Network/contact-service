package com.jtm.contact.core.domain.entity;

import com.jtm.contact.core.domain.dto.MessageDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
@Document("messages")
public class Message {

    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String message;
    private String domain;
    private String clientAddress;
    private Long sentTime;

    public Message(String firstName, String lastName, String emailAddress, String message, String domain, String clientAddress) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.message = message;
        this.domain = domain;
        this.clientAddress = clientAddress;
        this.sentTime = System.currentTimeMillis();
    }

    public Message(String firstName, String lastName, String emailAddress, String message, String domain, String clientAddress, Long sentTime) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.message = message;
        this.domain = domain;
        this.clientAddress = clientAddress;
        this.sentTime = sentTime;
    }

    public Message(MessageDto dto, String domain, String clientAddress) {
        this(dto.getFirstName(), dto.getLastName(), dto.getEmailAddress(), dto.getMessage(), domain, clientAddress);
    }

    /**
     * This will check if the same client address can message again,
     * only allowing 1 message per hour in the process.
     *
     * @return boolean      if the user can message again
     */
    public boolean canMessage() {
        return System.currentTimeMillis() > (this.sentTime + TimeUnit.HOURS.toMillis(1));
    }
}
