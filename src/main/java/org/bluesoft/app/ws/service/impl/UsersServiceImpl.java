package org.bluesoft.app.ws.service.impl;

import org.bluesoft.app.ws.service.UsersService;
import org.bluesoft.app.ws.shared.dto.UserDTO;

public class UsersServiceImpl implements UsersService {
    @Override
    public UserDTO createUser(UserDTO user) {
        UserDTO returnValue = user;
        return returnValue;
    }
}
