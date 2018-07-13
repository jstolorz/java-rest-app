package org.bluesoft.app.ws.exceptions;

import org.bluesoft.app.ws.ui.model.response.ErrorMessage;
import org.bluesoft.app.ws.ui.model.response.ErrorMessages;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class CouldNotUpdateRecordExceptionMapper implements ExceptionMapper<CouldNotUpdateRecordException> {

    @Override
    public Response toResponse(CouldNotUpdateRecordException exception) {

        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(),
                ErrorMessages.COULD_NOT_UPDATE_RECORD.name(),"http://localhost");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
    }
}
