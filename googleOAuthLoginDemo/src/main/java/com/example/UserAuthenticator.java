package com.example;

import com.example.core.utils.Registry;
import com.example.daos.UserLogin;
import com.example.models.User;
import com.example.services.UserService;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

public class UserAuthenticator implements Authenticator<BasicCredentials, User> {

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) {
        UserLogin userLogin = UserLogin.builder().email(credentials.getUsername()).password(credentials.getPassword()).build();
        UserService userService = Registry.getType(UserService.class);
        String token = userService.isValidUser(userLogin);
        if (token == null) {
            return Optional.empty();
        }
        return Optional.of(userService.getUserByToken(token));
    }

}
