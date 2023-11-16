package com.example.DAuction.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ErrorMessage {
    private String message;
    private LocalDateTime timeStamp;
    private boolean status;

}
