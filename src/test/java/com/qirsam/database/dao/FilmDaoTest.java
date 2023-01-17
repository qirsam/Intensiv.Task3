package com.qirsam.database.dao;

import com.qirsam.IntegrationTestBase;
import com.qirsam.database.entity.Film;
import com.qirsam.utils.ConnectionPool;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmDaoTest extends IntegrationTestBase {

    private static final int FILM_SIZE = 7;
    private static final Long FILM_ID = 3L;
    private static final String TEST_FILM_NAME = "Blade Runner 2049";
    private static final Long TEST_STUDIO_ID = 7L;

    private final FilmDao filmDao = FilmDao.getInstance();

    @Test
    void findAll() {
        try (Connection connection = ConnectionPool.get()) {
            List<Film> result = filmDao.findAll(connection);

            assertThat(result.size()).isEqualTo(FILM_SIZE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findById() {
        try (Connection connection = ConnectionPool.get()) {
            Optional<Film> mayBeFilm = filmDao.findById(FILM_ID, connection);

            assertTrue(mayBeFilm.isPresent());
            mayBeFilm.ifPresent(film -> assertThat(film.getName()).isEqualTo(TEST_FILM_NAME));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void save() {
        try (Connection connection = ConnectionPool.get()) {
            Film drive = new Film(
                    0L,
                    "Drive",
                    LocalDate.of(2011, 5, 20),
                    StudioDao.getInstance().findById(TEST_STUDIO_ID, connection).orElse(null)
            );
            Film result = filmDao.save(drive, connection);

            assertThat(result).isEqualTo(drive);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
        try (Connection connection = ConnectionPool.get()) {
            Optional<Film> mayBeFilm = filmDao.findById(FILM_ID, connection);
            mayBeFilm.get().setName(TEST_FILM_NAME + "!");

            filmDao.update(mayBeFilm.get(), connection);

            Optional<Film> resultFilm = filmDao.findById(FILM_ID, connection);

            assertThat(resultFilm)
                    .isPresent()
                    .get()
                    .satisfies(film -> assertThat(film.getName()).isEqualTo(TEST_FILM_NAME + "!"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void delete() {
        try (Connection connection = ConnectionPool.get()) {
            boolean result = filmDao.delete(FILM_ID, connection);
            assertThat(result).isTrue();

            int resultFilmSize = filmDao.findAll(connection).size();
            assertThat(resultFilmSize).isEqualTo(FILM_SIZE - 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findActorsByFilmId() {
    }

    @Test
    void addActorsIdByFilmId() {
    }
}