package ru.ttk.slotsbe.backend.exceptionhandler;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.ttk.slotsbe.backend.exception.ValidateException;


@ControllerAdvice
@OpenAPIDefinition(info = @Info(title = "API", version = "v1"))
@Hidden // из io.swagger.v3.oas.annotations.Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<AppError> handleValidateException(ValidateException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new AppError(e.getStatus().value(), e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AppError> handleConstraintViolation(ConstraintViolationException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AppError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    // Можно добавить другие ExceptionHandler'ы по необходимости
}
