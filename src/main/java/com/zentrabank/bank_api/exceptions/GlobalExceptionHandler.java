package com.zentrabank.bank_api.exceptions;

import com.zentrabank.bank_api.config.BankApiConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

// We use @ControllerAdvice to handle
// exceptions thrown by any controller in one central place.
@ControllerAdvice
public class GlobalExceptionHandler {
    private final BankApiConfigProperties config;
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(BankApiConfigProperties config){
        this.config = config;
    }

    private Boolean isDev() {
        return  config.profileActive().equals("dev");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "error");
        body.put("message", message);
        body.put("data", null);

        if (isDev()) {
            body.put("stack", ex.getStackTrace());
        }

        logger.error("{}", body);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Internal server error";

        if (ex instanceof ResponseStatusException rse) {
            status = HttpStatus.valueOf(rse.getStatusCode().value());
            message = rse.getReason();
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "error");
        body.put("message", message);
        body.put("data", null);

        if (isDev()) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            body.put("stack", sw.toString());
        }

        logger.error("{}", body);
        return  new ResponseEntity<>(body, status);
    }
}