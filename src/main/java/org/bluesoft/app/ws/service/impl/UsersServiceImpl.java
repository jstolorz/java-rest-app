package org.bluesoft.app.ws.service.impl;

import org.bluesoft.app.ws.exceptions.CouldNotCreateRecordException;
import org.bluesoft.app.ws.exceptions.CouldNotDeleteRecordException;
import org.bluesoft.app.ws.exceptions.CouldNotUpdateRecordException;
import org.bluesoft.app.ws.exceptions.NoRecordFoundException;
import org.bluesoft.app.ws.io.dao.DAO;
import org.bluesoft.app.ws.io.dao.impl.MySQLDAO;
import org.bluesoft.app.ws.service.UsersService;
import org.bluesoft.app.ws.shared.dto.UserDTO;
import org.bluesoft.app.ws.ui.model.response.ErrorMessages;
import org.bluesoft.app.ws.utils.UserProfileUtils;

import java.util.List;

public class UsersServiceImpl implements UsersService {

    DAO database;

    public UsersServiceImpl() {
        this.database = new MySQLDAO();
    }


    @Override
    public UserDTO createUser(UserDTO user) {

        UserDTO returnValue = null;

        UserProfileUtils profileUtils = new UserProfileUtils();

        profileUtils.validateRequiredFields(user);

        UserDTO existingUser = this.getUserByUserName(user.getEmail());

        if (existingUser != null) {
            throw new CouldNotCreateRecordException(ErrorMessages.RECORD_ALREADY_EXISTS.name());
        }

        String userId = profileUtils.generateUserId(30);
        user.setUserId(userId);

        String salt = profileUtils.getSalt(30);

        String encodedPassword = profileUtils.generateSecurePassword(user.getPassword(), salt);
        user.setSalt(salt);
        user.setEncryptedPassword(encodedPassword);
        user.setEmailVerificationStatus(true);
        user.setEmailVerificationToken(profileUtils.generateEmailverificationToken(30));

        returnValue = this.saveUser(user);


        return returnValue;
    }

    @Override
    public UserDTO getUser(String id) {

        UserDTO dto = null;

        try {

            this.database.openConnection();
            dto = this.database.getUser(id);

        }catch (Exception ex)
        {
            ex.printStackTrace();
            throw new NoRecordFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }finally {
            this.database.closeConnection();
        }

        return dto;
    }

    @Override
    public UserDTO getUserByUserName(String userName) {
        UserDTO userDTO = null;

        if (userName == null || userName.isEmpty()) {
            return userDTO;
        }

        try {
            this.database.openConnection();
            userDTO = this.database.getUserByUserName(userName);
        } finally {
            this.database.closeConnection();
        }

        return userDTO;
    }

    @Override
    public List<UserDTO> getUsers(int start, int limit) {

        List<UserDTO> users = null;

        try{
            database.openConnection();
            users = database.getUsers(start,limit);
        }finally {
            database.closeConnection();
        }

        return users;
    }

    @Override
    public void updateUserDetails(UserDTO userDetails) {

        try{
            database.openConnection();
            database.updateUser(userDetails);
        }catch (Exception ex){
            throw new CouldNotUpdateRecordException(ex.getMessage());
        }finally {
            database.closeConnection();
        }

    }

    @Override
    public void deleteUser(UserDTO userDto) {
        try{
            database.openConnection();
            database.deleteUser(userDto);
        }catch (Exception ex){
            throw new CouldNotDeleteRecordException(ex.getMessage());
        }finally {
            database.closeConnection();
        }

        try{
            userDto = getUser(userDto.getUserId());
        }catch (NoRecordFoundException ex){
            userDto = null;
        }

        if(userDto != null){
            throw new CouldNotDeleteRecordException(
                    ErrorMessages.COULD_NOT_DELETE_RECORD.getErrorMessage());
        }

    }

    @Override
    public boolean verifyEmail(String token) {
        return false;
    }


    private UserDTO saveUser(UserDTO user){
        UserDTO returnValue = null;

        try{
            this.database.openConnection();
            returnValue = this.database.saveUser(user);
        }finally {
            this.database.closeConnection();
        }

        return returnValue;
    }


}
