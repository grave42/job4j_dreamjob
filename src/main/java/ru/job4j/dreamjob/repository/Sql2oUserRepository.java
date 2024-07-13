package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.dreamjob.model.User;

import java.util.Optional;

@Repository
public class Sql2oUserRepository implements UserRepository {

    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) throws Sql2oException {
        try (var connection = sql2o.open()) {
            try {
                var sql = """
                        INSERT INTO users(email, password, name) VALUES (:email, :password, :name)
                        """;
                var query = connection.createQuery(sql, true)
                        .addParameter("email", user.getEmail())
                        .addParameter("password", user.getPassword())
                        .addParameter("name", user.getName());
                int generatedId = query.executeUpdate().getKey(Integer.class);
                user.setId(generatedId);
                return Optional.of(user);
            } catch (Sql2oException e) {
                throw e;
            }
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users WHERE email = :email and password = :password");
            query.addParameter("email", email);
            query.addParameter("password", password);
            var user = query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }
}
