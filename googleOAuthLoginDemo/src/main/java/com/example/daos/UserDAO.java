package com.example.daos;

import com.example.models.User;
import com.google.inject.Inject;
import org.jdbi.v3.core.Jdbi;

import java.util.Optional;

public class UserDAO {

    private final Jdbi jdbi;

    @Inject
    public UserDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.jdbi.registerRowMapper(new User.UserMapper());
    }

    public Long createUser(User user) {
        return jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO users values (:id, :name, :email, :password, :is_deleted, :token)")
                .bind("id", Optional.empty())
                .bind("name", user.getName())
                .bind("email", user.getEmail())
                .bind("password", user.getPassword())
                .bind("is_deleted", Boolean.FALSE)
                .bind("token", Optional.empty())
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).findFirst().orElse(null)
        );
    }

    public void updateUserToken(long id, String token) {
        jdbi.withHandle(handle -> handle.createUpdate("UPDATE users set token = :token where id = :id and is_deleted = 0")
                .bind("id", id)
                .bind("token", token)
                .execute()
        );
    }

    public User getUser(long id) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from users where id = :id and is_deleted = 0")
                .bind("id", id).mapTo(User.class).findFirst().orElse(null)
        );
    }

    public User getUserByEmail(String email) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from users where email = :email and is_deleted = 0")
                .bind("email", email).mapTo(User.class).findFirst().orElse(null)
        );
    }

    public User getUserByToken(String token) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from users where token = :token and is_deleted = 0")
                .bind("token", token).mapTo(User.class).findFirst().orElse(null)
        );
    }

    public void deleteUser(User user) {
        jdbi.withHandle(handle -> handle.createUpdate("UPDATE users set is_deleted = 1 where id = :id")
                .bind("id", user.getId()).execute());
    }
}
