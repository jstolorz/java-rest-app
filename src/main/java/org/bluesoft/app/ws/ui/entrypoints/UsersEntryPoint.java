package org.bluesoft.app.ws.ui.entrypoints;

import org.bluesoft.app.ws.service.UsersService;
import org.bluesoft.app.ws.service.impl.UsersServiceImpl;
import org.bluesoft.app.ws.shared.dto.UserDTO;
import org.bluesoft.app.ws.ui.model.request.CreateUserRequestModel;
import org.bluesoft.app.ws.ui.model.response.UserProfileRest;
import org.springframework.beans.BeanUtils;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/users")
public class UsersEntryPoint {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfileRest createUser(CreateUserRequestModel requestModel){
        UserProfileRest profileRest = new UserProfileRest();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(requestModel,userDTO);

        UsersService usersService = new UsersServiceImpl();
        UserDTO createdUserProfile = usersService.createUser(userDTO);

        BeanUtils.copyProperties(createdUserProfile, profileRest);

        return profileRest;
    }
}
