package org.bluesoft.app.ws.utils;

import org.bluesoft.app.ws.exceptions.MissingRequiredFieldException;
import org.bluesoft.app.ws.shared.dto.UserDTO;
import org.bluesoft.app.ws.ui.model.response.ErrorMessages;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

public class UserProfileUtils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final int ITERATIONS = 10000;
    private final int KEY_LENGTH = 256;


    public String generateUUID(){

        String returnValue = UUID.randomUUID().toString().replaceAll("-","");

        return returnValue;
    }

    private String generateRandomString(int length){
        StringBuilder returnValue = new StringBuilder(length);

        for(int i = 0; i < length; i++){
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(returnValue);
    }

    public String generateUserId(int length){
        return generateRandomString(length);
    }

    public String generateEmailverificationToken(int length){
        return generateRandomString(length);
    }


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


    public String getSalt(int length) {
        return generateRandomString(length);
    }


    public String generateSecurePassword(String password, String salt){
        String returnValue = null;

        byte[] securedPassword = hash(password.toCharArray(), salt.getBytes());

        returnValue = Base64.getEncoder().encodeToString(securedPassword);

        return returnValue;
    }

    public byte[] hash(char[] password, byte[] salt){
        PBEKeySpec spec = new PBEKeySpec(password,salt,ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try{
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        }catch (NoSuchAlgorithmException e){
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        }catch (InvalidKeySpecException e){
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        }finally {
            spec.clearPassword();
        }
    }




}
