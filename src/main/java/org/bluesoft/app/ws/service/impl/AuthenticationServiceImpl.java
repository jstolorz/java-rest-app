package org.bluesoft.app.ws.service.impl;

import org.bluesoft.app.ws.exceptions.AuthenticationException;
import org.bluesoft.app.ws.exceptions.EmailVerificationException;
import org.bluesoft.app.ws.io.dao.DAO;
import org.bluesoft.app.ws.service.AuthenticationService;
import org.bluesoft.app.ws.service.UsersService;
import org.bluesoft.app.ws.shared.dto.UserDTO;
import org.bluesoft.app.ws.ui.model.response.ErrorMessages;
import org.bluesoft.app.ws.utils.UserProfileUtils;

public class AuthenticationServiceImpl implements AuthenticationService {

    private DAO database;

    @Override
    public UserDTO authenticate(String userName, String password) throws AuthenticationException {

        UsersService usersService = new UsersServiceImpl();
        UserDTO storedUser = usersService.getUserByUserName(userName);

        if(storedUser == null){
            throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
        }

        if(!storedUser.getEmailVerificationStatus()){
           throw new EmailVerificationException(ErrorMessages.EMAIL_ADDRESS_NOT_VERIFIED.getErrorMessage());
        }


        String encryptedPassword = null;

        encryptedPassword = new UserProfileUtils().generateSecurePassword(password, storedUser.getSalt());

        boolean authenticated = false;

        if(encryptedPassword != null && encryptedPassword.equalsIgnoreCase(storedUser.getEncryptedPassword())){
            if(userName != null && userName.equalsIgnoreCase(storedUser.getEmail())){
                authenticated = true;
            }
        }

        if(!authenticated){
            throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
        }

        return storedUser;
    }
}
