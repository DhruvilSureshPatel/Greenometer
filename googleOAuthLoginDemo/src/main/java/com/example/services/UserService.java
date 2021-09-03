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
            throw new CustomException(String.format("user with email:%s already exists", user.getEmail()));
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
        log.debug("user with email:{} is Valid. Token generated is: {}", user.getEmail(), uuid);
        userDAO.updateUserToken(userFound.getId(), uuid.toString());
        return uuid.toString();
    }

    public Optional<User> authenticateUser(User user) {
        User userFound = userDAO.getUserByToken(user.getToken());
        if (userFound == null || !userFound.getEmail().equals(user.getEmail())) {
            log.debug("failed to authenticate user with mail:{} and token:{}", user.getEmail(), user.getToken());
            return Optional.empty();
        }
        log.debug("User with email:{} and token:{} authenticated successfully", user.getEmail(), user.getToken());
        return Optional.of(userFound);
    }

    public User getUserByToken(String token) {
        return userDAO.getUserByToken(token);
    }

    public User getUserById(Long userId) {
        return userDAO.getUser(userId);
    }

    public void deleteUser(User user) {
        log.debug("trying to delete user with email:{}", user.getEmail());
        userDAO.deleteUser(user);
    }

    public void updateRewardPoints(User user, long points, boolean toBeAdded) throws CustomException {
        String message = "trying to ";
        if (toBeAdded) {
            user.setRewardPoints(user.getRewardPoints() + points);
            message += "add ";
        } else {
            long temp = user.getRewardPoints();
            user.setRewardPoints(user.getRewardPoints() - points);
            if (user.getRewardPoints() < 0) {
                throw new CustomException(String.format("User does not have sufficient reward point to deduct. " +
                        "Current points=%s, to be deducted=%s", temp, points));
            }
            message += "deduct ";
        }
        log.debug("{} {} reward points from user with email:{}", message, points, user.getEmail());
        userDAO.updateRewardPoints(user.getRewardPoints(), user.getId());
    }
}
