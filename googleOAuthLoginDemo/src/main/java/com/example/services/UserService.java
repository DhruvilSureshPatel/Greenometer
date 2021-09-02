package com.example.services;

import com.example.core.exceptions.CustomException;
import com.example.daos.UserDAO;
import com.example.daos.UserLogin;
import com.example.models.User;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class UserService {

    private UserDAO userDAO;

    @Inject
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Long createNewUser(User user) throws CustomException {
        User userFound = userDAO.getUserByEmail(user.getEmail());
        if (userFound != null) {
            throw new CustomException(String.format("User with email:%s already exists", user.getEmail()));
        }

        log.debug("trying to create user with name:{}, email:{}", user.getName(), user.getEmail());
        return userDAO.createUser(user);
    }

    public String isValidUser(UserLogin user) {
        User userFound = userDAO.getUserByEmail(user.getEmail());
        if (userFound == null || !userFound.getPassword().equals(user.getPassword())) {
            return null;
        }
        UUID uuid = UUID.randomUUID();
        userDAO.updateUserToken(userFound.getId(), uuid.toString());
        return uuid.toString();
    }

    public Optional<User> authenticateUser(User user) {
        User userFound = userDAO.getUserByToken(user.getToken());
        if (userFound == null || !userFound.getEmail().equals(user.getEmail())) {
            return Optional.empty();
        }
        return Optional.of(userFound);
    }

    public void deleteUser(User user) {
        userDAO.deleteUser(user);
    }
}
