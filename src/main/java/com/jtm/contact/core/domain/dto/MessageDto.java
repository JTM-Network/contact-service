package com.jtm.contact.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String message;
    private String domain;
}
