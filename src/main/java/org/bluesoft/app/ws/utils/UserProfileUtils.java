package org.bluesoft.app.ws.utils;

import org.bluesoft.app.ws.exceptions.MissingRequiredFieldException;
import org.bluesoft.app.ws.shared.dto.UserDTO;
import org.bluesoft.app.ws.ui.model.response.ErrorMessages;

public class UserProfileUtils {

    public void validateRequiredFields(UserDTO userDTO) throws MissingRequiredFieldException {

        if(userDTO.getFirstName() == null
                || userDTO.getFirstName().isEmpty()
                || userDTO.getLastName() == null
                || userDTO.getLastName().isEmpty()
                || userDTO.getEmail() == null
                || userDTO.getEmail().isEmpty()
                || userDTO.getPassword() == null
                || userDTO.getPassword().isEmpty()){
            throw new MissingRequiredFieldException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

    }


}
