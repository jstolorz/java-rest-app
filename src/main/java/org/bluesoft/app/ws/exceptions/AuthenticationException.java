package org.bluesoft.app.ws.exceptions;

public class AuthenticationException extends RuntimeException {
    private static final long serialVersionUID = 7820962767102605578L;

    public AuthenticationException(String message){
        super(message);
    }
}
