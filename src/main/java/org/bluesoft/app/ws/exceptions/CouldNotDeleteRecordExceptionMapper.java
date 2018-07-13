package org.bluesoft.app.ws.exceptions;

import org.bluesoft.app.ws.ui.model.response.ErrorMessage;
import org.bluesoft.app.ws.ui.model.response.ErrorMessages;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CouldNotDeleteRecordExceptionMapper implements ExceptionMapper<CouldNotDeleteRecordException> {

    @Override
    public Response toResponse(CouldNotDeleteRecordException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),
                ErrorMessages.COULD_NOT_DELETE_RECORD.name(), "http://appsdeveloperblog.com");

        return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
    }
}
