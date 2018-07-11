package org.bluesoft.app.ws.io.dao;

import org.bluesoft.app.ws.shared.dto.UserDTO;

import java.util.List;

public interface DAO {

    void openConnection();
    UserDTO getUserByUserName(String userName);
    UserDTO saveUser(UserDTO user);
    UserDTO getUser(String id);
    List<UserDTO> getUsers(int start, int limit);
    void updateUser(UserDTO userProfile);
    void deleteUser(UserDTO userProfile);
    UserDTO getUserByEmailToken(String token);
    void closeConnection();

}
