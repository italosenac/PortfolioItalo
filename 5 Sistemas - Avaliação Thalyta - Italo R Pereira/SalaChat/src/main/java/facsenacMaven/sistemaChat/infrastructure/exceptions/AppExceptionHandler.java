package facsenacMaven.sistemaChat.infrastructure.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<Object> RequestExceptionHandler(RequestException exception, WebRequest request) {
    return GlobalExceptionHandler(
        exception,
        exception.getErrorCode(),
        exception.getMessage(),
            null,
            BAD_REQUEST,
            request);
    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> GenericExceptionHandler(Exception exception, WebRequest request) {
    return GlobalExceptionHandler(
            exception,
            null,
            exception.getMessage(),
            null,
            INTERNAL_SERVER_ERROR,
            request);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request){
        List<String> details = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(Objects::nonNull)
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return GlobalExceptionHandler(exception, "ValidationError", null, details, BAD_REQUEST, request);
    }
    private ResponseEntity<Object> GlobalExceptionHandler(Exception exception,
                                                          String errorCode,
                                                          String message,
                                                          List<String> details,
                                                          HttpStatus status,
                                                          WebRequest request) {

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;

        return handleExceptionInternal(
                exception,
                RestError.builder()
                        .errorCode(errorCode)
                        .errorMessage(message)
                        .details(details)
                        .status(status.value())
                        .path(servletWebRequest.getRequest().getRequestURI())
                        .build(),

                new HttpHeaders(),
                status,
                request);
    }

}
