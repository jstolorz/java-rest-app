package org.bluesoft.app.ws.ui.entrypoints;

import org.bluesoft.app.ws.service.AuthenticationService;
import org.bluesoft.app.ws.service.impl.AuthenticationServiceImpl;
import org.bluesoft.app.ws.shared.dto.UserDTO;
import org.bluesoft.app.ws.ui.model.request.LoginCredentials;
import org.bluesoft.app.ws.ui.model.response.AuthenticationDetails;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/authentication")
public class AuthenticationEntryPoint {


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public AuthenticationDetails userLogin(LoginCredentials loginCredentials){

        AuthenticationDetails authenticationDetails = new AuthenticationDetails();

        AuthenticationService authenticationService = new AuthenticationServiceImpl();
        UserDTO authenticatedUser = authenticationService.authenticate(loginCredentials.getUserName(), loginCredentials.getUserPassword());

        authenticationService.resetSecurityCridentials(loginCredentials.getUserPassword(), authenticatedUser);

        String accessToken = authenticationService.issueAccessToken(authenticatedUser);

        authenticationDetails.setId(authenticatedUser.getUserId());
        authenticationDetails.setToken(accessToken);

        return authenticationDetails;
    }

}
