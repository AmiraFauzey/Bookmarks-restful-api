package com.mira.bookmarkService.exceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvisor.class);

    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<Object> InvalidDataAccessResourceUsageException(
            InvalidDataAccessResourceUsageException ex, WebRequest request) {
        LOGGER.error(ex.toString());
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    /*
     * @ExceptionHandler(Exception.class) public ResponseEntity<Object>
     * exception(Exception ex, WebRequest request) { LOGGER.error(ex.toString());
     * return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }
     */

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        logError(ex);
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //to know where the error is happens
    private void logError(Exception exception){
        StackTraceElement[] stackTrace = exception.getStackTrace();
        for(StackTraceElement e : stackTrace){
            LOGGER.error(e.toString());
        }
    }
}
