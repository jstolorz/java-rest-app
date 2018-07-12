package org.bluesoft.app.ws.filters;

import org.bluesoft.app.ws.annotations.Secured;
import org.bluesoft.app.ws.service.UsersService;
import org.bluesoft.app.ws.service.impl.UsersServiceImpl;
import org.bluesoft.app.ws.shared.dto.UserDTO;
import org.bluesoft.app.ws.utils.UserProfileUtils;

import javax.annotation.Priority;
import javax.security.sasl.AuthenticationException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
         String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

         if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer")){
             throw new org.bluesoft.app.ws.exceptions.AuthenticationException("Authorization header must be provided");
         }

         String token = authorizationHeader.substring("Bearer".length()).trim();
         String userId = requestContext.getUriInfo().getPathParameters().getFirst("id");

         validateToken(token,userId);
    }

    private void validateToken(String token, String userId) throws AuthenticationException {

        UsersService service = new UsersServiceImpl();
        UserDTO userDTO = service.getUser(userId);

        String completeToken = userDTO.getToken() + token;

        String securePassword = userDTO.getEncryptedPassword();
        String salt = userDTO.getSalt();
        String accessTokenMaterial = userId + salt;
        byte[] encryptedAccessToken = null;

        try{
            encryptedAccessToken = new UserProfileUtils().encrypt(securePassword, accessTokenMaterial);
        }catch (InvalidKeySpecException ex){
            Logger.getLogger(AuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
            throw new org.bluesoft.app.ws.exceptions.AuthenticationException("Failed to issue secure access token");
        }

        String encryptedAccessTokenBase64Encoded = Base64.getEncoder().encodeToString(encryptedAccessToken);

        if(!encryptedAccessTokenBase64Encoded.equalsIgnoreCase(completeToken)){
            throw new AuthenticationException("Authorization token did not match");
        }

    }
}
