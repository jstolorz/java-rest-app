package org.bluesoft.app.ws.ui.entrypoints;

import org.bluesoft.app.ws.annotations.Secured;
import org.bluesoft.app.ws.service.UsersService;
import org.bluesoft.app.ws.service.impl.UsersServiceImpl;
import org.bluesoft.app.ws.shared.dto.UserDTO;
import org.bluesoft.app.ws.ui.model.request.CreateUserRequestModel;
import org.bluesoft.app.ws.ui.model.response.UserProfileRest;
import org.springframework.beans.BeanUtils;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


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

    @Secured
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfileRest getUserProfile(@PathParam("id") String id){
        UserProfileRest returnValue = null;

        UsersService service = new UsersServiceImpl();
        UserDTO userDTO = service.getUser(id);

        returnValue = new UserProfileRest();

        BeanUtils.copyProperties(userDTO, returnValue);

        return returnValue;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<UserProfileRest> getUsers(@DefaultValue("0") @QueryParam("start") int start,
                                          @DefaultValue("50") @QueryParam("limit") int limit){
        List<UserProfileRest> returnValue = new ArrayList<>();
        UsersService service = new UsersServiceImpl();

        List<UserDTO> users = service.getUsers(start,limit);

        for (UserDTO userDTO : users) {
            UserProfileRest profileRest = new UserProfileRest();
            BeanUtils.copyProperties(userDTO, profileRest);
            profileRest.setHref("/users/" + userDTO.getUserId());
            returnValue.add(profileRest);
        }


        return returnValue;
    }
}
