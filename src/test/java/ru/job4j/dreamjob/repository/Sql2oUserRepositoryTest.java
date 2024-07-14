package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.util.Properties;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository repository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oCandidateRepository.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        repository = new Sql2oUserRepository(sql2o);
    }

    @Test
    public void whenSaveThenGetSame() {
        var user = repository.save(new User(0, "email2", "password", "name"));
        var savedUser = repository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword());

        assertThat(savedUser.get()).usingRecursiveComparison().isEqualTo(user.get());
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        var user = new User(0, "email4", "password4", "name4");
        var savedUser = repository.findByEmailAndPassword(user.getEmail(), user.getPassword());

        assertThat(savedUser.equals(empty()));
    }

    @Test
    public void whenSaveWithSameEmailThenThrowsException() {
        repository.save(new User(1, "emailaaa", "password3", "name3"));
        var user2 = repository.save(new User(2, "emailaaa", "password2", "name2"));
        assertTrue(user2.isEmpty());
    }

}