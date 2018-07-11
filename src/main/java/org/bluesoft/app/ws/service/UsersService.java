package org.bluesoft.app.ws.service;

import org.bluesoft.app.ws.shared.dto.UserDTO;

import java.util.List;

public interface UsersService {

    UserDTO createUser(UserDTO user);
    UserDTO getUser(String id);
    UserDTO getUserByUserName(String userName);
    List<UserDTO> getUsers(int start, int limit);
    void updateUserDetails(UserDTO userDetails);
    void deleteUser(UserDTO userDto);
    boolean verifyEmail(String token);

}
