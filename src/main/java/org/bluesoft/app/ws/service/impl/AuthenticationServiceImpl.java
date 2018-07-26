package org.bluesoft.app.ws.service.impl;

import org.bluesoft.app.ws.exceptions.AuthenticationException;
import org.bluesoft.app.ws.exceptions.EmailVerificationException;
import org.bluesoft.app.ws.io.dao.DAO;
import org.bluesoft.app.ws.io.dao.impl.MySQLDAO;
import org.bluesoft.app.ws.service.AuthenticationService;
import org.bluesoft.app.ws.service.UsersService;
import org.bluesoft.app.ws.shared.dto.UserDTO;
import org.bluesoft.app.ws.ui.model.response.ErrorMessages;
import org.bluesoft.app.ws.utils.UserProfileUtils;

import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationServiceImpl implements AuthenticationService {

    private UsersService usersService;
    private DAO database;

    public AuthenticationServiceImpl(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public UserDTO authenticate(String userName, String password) throws AuthenticationException {

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

    @Override
    public String issueAccessToken(UserDTO userProfile) throws AuthenticationException {
        String returnValue = null;

        String newSaltAsPrefix = userProfile.getSalt();
        String accessTokenMaterial = userProfile.getUserId() + newSaltAsPrefix;

        byte[] encryptedAccessToken = null;

        try{
             encryptedAccessToken = new UserProfileUtils().encrypt(userProfile.getEncryptedPassword(), accessTokenMaterial);
        }catch (InvalidKeySpecException ex){
            Logger.getLogger(AuthenticationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new AuthenticationException("Failed to issue secure access token");
        }

        String encryptedAccessTokenBase64Encoded = Base64.getEncoder().encodeToString(encryptedAccessToken);

        int tokenLength = encryptedAccessTokenBase64Encoded.length();

        String tokenToSaveToDatabase = encryptedAccessTokenBase64Encoded.substring(0, tokenLength/2);
        returnValue = encryptedAccessTokenBase64Encoded.substring(tokenLength/2,tokenLength);

        userProfile.setToken(tokenToSaveToDatabase);
        updateUserProfile(userProfile);

        return returnValue;
    }

    @Override
    public void resetSecurityCridentials(String password, UserDTO userProfile) {
        UserProfileUtils profileUtils = new UserProfileUtils();
        String salt = profileUtils.getSalt(30);

        String securePassword = profileUtils.generateSecurePassword(password, salt);
        userProfile.setSalt(salt);
        userProfile.setEncryptedPassword(securePassword);

        updateUserProfile(userProfile);
    }

    private void updateUserProfile(UserDTO userDTO){

        database = new MySQLDAO();

        try{
            database.openConnection();
            database.updateUser(userDTO);
        }finally {
            database.closeConnection();
        }
    }


}
