package com.example;

import com.example.models.User;
import io.dropwizard.auth.Authorizer;

public class UserAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        // no role based authorization done
        return true;
    }

}
