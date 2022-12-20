package com.accenture.codingtest.springbootcodingtest.exceptions;
import com.accenture.codingtest.springbootcodingtest.model.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                            WebRequest webRequest) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage()
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceExistException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceExistsException(ResourceExistException exception,
                                                                            WebRequest webRequest) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage()
        );
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleInternalServerErrorException(Exception ex) {

        if (ex instanceof NullPointerException) {
            return new ResponseEntity<>(new ApiErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "An Unexpected Error has Occured!"
            ),HttpStatus.BAD_REQUEST);
        } else if (ex instanceof HttpRequestMethodNotSupportedException){
            return new ResponseEntity<>(new ApiErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.getMessage(),
                    "Unsupported Http Method"
            ),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An Unexpected Error has Occured!"
        ),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
