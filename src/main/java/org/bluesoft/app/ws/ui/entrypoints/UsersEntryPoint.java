package org.bluesoft.app.ws.ui.entrypoints;

import org.bluesoft.app.ws.annotations.Secured;
import org.bluesoft.app.ws.service.UsersService;
import org.bluesoft.app.ws.service.impl.UsersServiceImpl;
import org.bluesoft.app.ws.shared.dto.UserDTO;
import org.bluesoft.app.ws.ui.model.request.CreateUserRequestModel;
import org.bluesoft.app.ws.ui.model.request.UpdateUserRequestModel;
import org.bluesoft.app.ws.ui.model.response.DeleteUserProfileResponseModel;
import org.bluesoft.app.ws.ui.model.response.RequestOperation;
import org.bluesoft.app.ws.ui.model.response.ResponseStatus;
import org.bluesoft.app.ws.ui.model.response.UserProfileRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;


@Path("/users")
public class UsersEntryPoint {

    @Autowired
    UsersService userService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfileRest createUser(CreateUserRequestModel requestModel){
        UserProfileRest profileRest = new UserProfileRest();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(requestModel,userDTO);


        UserDTO createdUserProfile = userService.createUser(userDTO);

        BeanUtils.copyProperties(createdUserProfile, profileRest);

        return profileRest;
    }

    @Secured
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfileRest getUserProfile(@PathParam("id") String id){
        UserProfileRest returnValue = null;


        UserDTO userDTO = userService.getUser(id);

        returnValue = new UserProfileRest();

        BeanUtils.copyProperties(userDTO, returnValue);

        return returnValue;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<UserProfileRest> getUsers(@DefaultValue("0") @QueryParam("start") int start,
                                          @DefaultValue("50") @QueryParam("limit") int limit){
        List<UserProfileRest> returnValue = new ArrayList<>();


        List<UserDTO> users = userService.getUsers(start,limit);

        for (UserDTO userDTO : users) {
            UserProfileRest profileRest = new UserProfileRest();
            BeanUtils.copyProperties(userDTO, profileRest);
            profileRest.setHref("/users/" + userDTO.getUserId());
            returnValue.add(profileRest);
        }


        return returnValue;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfileRest updateUserDetails(@PathParam("id") String id,
                                             UpdateUserRequestModel model){


        UserDTO user = userService.getUser(id);

        if(model.getFirstName() != null && !model.getFirstName().isEmpty()){
            user.setFirstName(model.getFirstName());
        }

        if(model.getLastName() != null && !model.getLastName().isEmpty()){
            user.setLastName(model.getLastName());
        }

        userService.updateUserDetails(user);

        UserProfileRest profileRest = new UserProfileRest();
        BeanUtils.copyProperties(user, profileRest);

        return profileRest;
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeleteUserProfileResponseModel deleteUserProfile(@PathParam("id") String id){

        DeleteUserProfileResponseModel model = new DeleteUserProfileResponseModel();
        model.setRequestOperation(RequestOperation.DELETE);

        UserDTO user = userService.getUser(id);

        userService.deleteUser(user);

        model.setResponseStatus(ResponseStatus.SUCCESS);

        return model;
    }


}
