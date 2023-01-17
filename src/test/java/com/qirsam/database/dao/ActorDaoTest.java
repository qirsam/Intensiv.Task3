package com.qirsam.database.dao;

import com.qirsam.IntegrationTestBase;
import com.qirsam.database.entity.Actor;
import com.qirsam.utils.ConnectionPool;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActorDaoTest extends IntegrationTestBase {

    private static final Long TEST_ACTOR_ID = 5L;
    private static final String TEST_ACTOR_FIRSTNAME = "testUpdate";
    private static final int ACTORS_SIZE = 18;
    private final ActorDao actorDao = ActorDao.getInstance();


    @Test
    void findById() {
        try (Connection connection = ConnectionPool.get()) {
            Optional<Actor> mayBeResult = actorDao.findById(1L, connection);
            assertTrue(mayBeResult.isPresent());
            mayBeResult.ifPresent(actor -> assertEquals(actor.getFirstname(), "Tom"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void save() {
        try (Connection connection = ConnectionPool.get()) {
            Actor actorTest = new Actor(
                    0L,
                    "Daniel",
                    "Day-Lewis",
                    LocalDate.of(1957, 4, 29),
                    "male"
            );
            Actor result = actorDao.save(actorTest, connection);

            assertThat(result).isEqualTo(actorTest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findAll() {
        try (Connection connection = ConnectionPool.get()) {
            List<Actor> allActors = actorDao.findAll(connection);

            assertThat(allActors.size()).isEqualTo(ACTORS_SIZE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
        try (Connection connection = ConnectionPool.get()) {
            Optional<Actor> actorById = actorDao.findById(TEST_ACTOR_ID, connection);
            actorById.get().setFirstname(TEST_ACTOR_FIRSTNAME);

            actorDao.update(actorById.get(), connection);

            Optional<Actor> resultActor = actorDao.findById(TEST_ACTOR_ID, connection);

            assertThat(resultActor)
                    .isPresent()
                    .get()
                    .satisfies(actor -> assertThat(actor.getFirstname()).isEqualTo(TEST_ACTOR_FIRSTNAME));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete() {
        try (Connection connection = ConnectionPool.get()) {

            boolean result = actorDao.delete(TEST_ACTOR_ID, connection);
            List<Actor> listResult = actorDao.findAll(connection);

            assertThat(result).isTrue();
            assertThat(listResult)
                    .hasSize(ACTORS_SIZE - 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}