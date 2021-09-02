package com.example.core.exceptions;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class CustomException extends Exception {

    public CustomException() {
        super("Unknown error");
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);

        if(cause instanceof InterruptedException) {
            log.debug("it was an interrupted exception. interrupting thread again.");
            Thread.currentThread().interrupt();
        }
    }

    public CustomException(Throwable cause) {
        super(cause);

        if(cause instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    public Response.Status getStatus() {
        return Response.Status.INTERNAL_SERVER_ERROR;
    }

    public int getErrorCode() {
        return 0;
    }

    public RuntimeException toRunTimeException() {
        return new RuntimeException(getMessage(), getCause());
    }
}
