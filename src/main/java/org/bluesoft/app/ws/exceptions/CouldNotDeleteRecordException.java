package org.bluesoft.app.ws.exceptions;

public class CouldNotDeleteRecordException extends RuntimeException {

    private static final long serialVersionUID = -2405714333928481237L;

    public CouldNotDeleteRecordException(String message) {
        super(message);
    }
}
