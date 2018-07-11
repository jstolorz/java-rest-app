package org.bluesoft.app.ws.exceptions;

public class EmailVerificationException  extends RuntimeException{
    private static final long serialVersionUID = 3582216814674534283L;

    public EmailVerificationException(String message)
    {
        super(message);
    }
}
