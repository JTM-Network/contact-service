package com.jtm.contact.core.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Failed to get remote address.")
public class FailedRemoteAddress extends RuntimeException {}
