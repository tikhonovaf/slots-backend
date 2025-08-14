package ru.ttk.slotsbe.backend.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.ttk.slotsbe.backend.exception.ValidateException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<AppError> catchResourceNotFoundException(ValidateException e) {
        return new ResponseEntity<>(new AppError(e.getStatus().value(), e.getMessage()), e.getStatus());
    }
}
