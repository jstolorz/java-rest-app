package org.bluesoft.app.ws.exceptions;

public class MissingRequiredFieldException extends RuntimeException {

    private static final long serialVersionUID = 3557432384965131371L;


    public MissingRequiredFieldException(String message)
    {
        super(message);
    }

}
