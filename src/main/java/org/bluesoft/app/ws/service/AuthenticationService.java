package org.bluesoft.app.ws.service;


import org.bluesoft.app.ws.exceptions.AuthenticationException;
import org.bluesoft.app.ws.shared.dto.UserDTO;

public interface AuthenticationService {

    UserDTO authenticate(String userName, String password) throws AuthenticationException;
    String issueAccessToken(UserDTO userProfile) throws AuthenticationException;
    public void resetSecurityCridentials(String password, UserDTO userProfile);
}
