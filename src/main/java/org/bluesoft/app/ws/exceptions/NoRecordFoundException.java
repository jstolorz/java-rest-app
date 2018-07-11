package org.bluesoft.app.ws.exceptions;

public class NoRecordFoundException extends RuntimeException {


    private static final long serialVersionUID = 3632346581753383112L;

    public NoRecordFoundException(String message){
        super(message);
    }
}
