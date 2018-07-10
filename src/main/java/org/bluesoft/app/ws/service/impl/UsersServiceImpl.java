package org.bluesoft.app.ws.service.impl;

import org.bluesoft.app.ws.service.UsersService;
import org.bluesoft.app.ws.shared.dto.UserDTO;
import org.bluesoft.app.ws.utils.UserProfileUtils;

public class UsersServiceImpl implements UsersService {
    @Override
    public UserDTO createUser(UserDTO user) {

        UserProfileUtils profileUtils = new UserProfileUtils();

        profileUtils.validateRequiredFields(user);

        UserDTO returnValue = user;
        return returnValue;
    }
}
