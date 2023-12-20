package com.userservice.utils;

import com.userservice.entity.UserEntity;
import com.userservice.model.request.AuthenticationRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.regex.Pattern;

import static com.userservice.utils.EncryptPassword.createSecretKey;
import static com.userservice.utils.EncryptPassword.decrypt;

@Component
public class AuthenticationUtils {

    @Value("${user.password.salt}")
    private String salt;

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public boolean isValidCredentials(UserEntity userEntity, AuthenticationRequest authenticationRequest,
                                      JSONObject obj) throws Exception {
        byte[] saltKey = salt.getBytes();
        SecretKeySpec key = createSecretKey(authenticationRequest.getPassword().toCharArray(), saltKey, 40000, 128);
        try {
            String decryptedPassword = decrypt(userEntity.getEncryptedPassword(), key);
            return decryptedPassword.equals(authenticationRequest.getPassword())
                    && userEntity.getUserName().equals(authenticationRequest.getUserName());
        } catch (Exception e) {
            throw new Exception(obj.toString());
        }

    }
}
