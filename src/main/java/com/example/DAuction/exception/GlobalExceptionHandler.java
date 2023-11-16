package com.example.DAuction.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.UnknownHostException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> handleUserException(UserException userException){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(userException.getMessage())
                .timeStamp(LocalDateTime.now())
                .status(false)
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<?> handleUnknownHostException(UnknownHostException unknownHostException){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(unknownHostException.getMessage())
                .timeStamp(LocalDateTime.now())
                .status(false)
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<?> handleJsonProcessingException(JsonProcessingException processingException){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(processingException.getMessage())
                .timeStamp(LocalDateTime.now())
                .status(false)
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

}
