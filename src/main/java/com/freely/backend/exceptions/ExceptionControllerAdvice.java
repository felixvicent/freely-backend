package com.freely.backend.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageExceptionHandler> resourceNotFound(ResourceNotFoundException resourceNotFoundException) {
        MessageExceptionHandler error = new MessageExceptionHandler(new Date(), HttpStatus.NOT_FOUND.value(),
                resourceNotFoundException.getMessage());

        return new ResponseEntity<MessageExceptionHandler>(error, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler({NoHasPermissionException.class, UnauthorizedException.class})
    public ResponseEntity<MessageExceptionHandler> noHasPermission(NoHasPermissionException noHasPermissionException) {
        MessageExceptionHandler error = new MessageExceptionHandler(new Date(), HttpStatus.FORBIDDEN.value(),
                noHasPermissionException.getMessage());

        return new ResponseEntity<MessageExceptionHandler>(error, HttpStatus.FORBIDDEN);
    }

    @ResponseBody
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<MessageExceptionHandler> resourceAlreadyExists(
            ResourceAlreadyExistsException resourceAlreadyExistsException) {
        MessageExceptionHandler error = new MessageExceptionHandler(new Date(), HttpStatus.CONFLICT.value(),
                resourceAlreadyExistsException.getMessage());

        return new ResponseEntity<MessageExceptionHandler>(error, HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class,
            BadRequestException.class})
    public ResponseEntity<MessageExceptionHandler> methodArgumentNotValid(
            Exception exception) {
        MessageExceptionHandler error = new MessageExceptionHandler(new Date(), HttpStatus.BAD_REQUEST.value(),
                exception.getMessage());

        return new ResponseEntity<MessageExceptionHandler>(error, HttpStatus.BAD_REQUEST);
    }
}
