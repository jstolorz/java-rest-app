package org.bluesoft.app.ws.exceptions;

import org.bluesoft.app.ws.ui.model.response.ErrorMessage;
import org.bluesoft.app.ws.ui.model.response.ErrorMessages;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {
    @Override
    public Response toResponse(AuthenticationException exception) {

        ErrorMessage errorMessage = new ErrorMessage(
                exception.getMessage(),
                ErrorMessages.AUTHENTICATION_FAILED.name(),
                "http://appsdeveloperblog.com"
        );

        return Response.status(Response.Status.UNAUTHORIZED).
                entity(errorMessage).
                build();

    }
}
