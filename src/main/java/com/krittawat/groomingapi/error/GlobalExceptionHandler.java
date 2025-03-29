package com.krittawat.groomingapi.error;

import com.krittawat.groomingapi.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleBadRequestException(BadRequestException ex) {
        return Response.builder()
                .code(400)
                .data(null)
                .message(ex.getMessage()).build();
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleNotFoundException(DataNotFoundException ex) {
        return Response.builder()
                .code(404)
                .data(null)
                .message(ex.getMessage()).build();
    }

    @ExceptionHandler({UnauthorizedException.class, TokenExpiredException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response handleUnauthorizedException(DataNotFoundException ex) {
        return Response.builder()
                .code(401)
                .data(null)
                .message(ex.getMessage()).build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // กรณี Exception อื่นๆ
    public Response handleGenericException(Exception ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        String errorDetails = stackTrace[0].getFileName() + ":" + stackTrace[0].getLineNumber();
        log.error("Exception occurred: [{}] : [{}]", errorDetails, ex.getMessage(), ex);
        return Response.builder()
                .code(500)
                .data(null)
                .message("["+errorDetails + "] : [" +ex.getMessage() + "]")
                .build();
    }
}