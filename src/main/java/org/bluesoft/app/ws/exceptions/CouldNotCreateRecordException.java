package org.bluesoft.app.ws.exceptions;

public class CouldNotCreateRecordException extends RuntimeException {

    private static final long serialVersionUID = 400151318341916531L;

    public CouldNotCreateRecordException(String name) {
        super(name);
    }
}
