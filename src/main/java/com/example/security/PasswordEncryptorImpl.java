package com.example.security;

import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptorImpl implements PasswordEncryptor {

    @Override
    public String encrypt(String password) {
       // return password;
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean matches(String password, String storedHash) {
      //  return password.equals(storedHash);
        return BCrypt.checkpw(password, storedHash);
    }
}
