package com.jtm.contact.core.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS, reason = "Only can message once per hour.")
public class MessageLimitReached extends RuntimeException {}
