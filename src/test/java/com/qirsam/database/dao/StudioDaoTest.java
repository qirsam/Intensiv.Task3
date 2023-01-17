package com.qirsam.database.dao;

import com.qirsam.IntegrationTestBase;
import com.qirsam.database.entity.Studio;
import com.qirsam.utils.ConnectionPool;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StudioDaoTest extends IntegrationTestBase {

    private static final StudioDao studioDao = StudioDao.getInstance();
    private static final Long STUDIO_ID = 3L;
    private static final String STUDIO_NAME = "Warner Bros";
    private static final int SIZE_ALL_STUDIOS = 7;

    @Test
    void findById() {
        try (Connection connection = ConnectionPool.get()) {
            Optional<Studio> result = studioDao.findById(STUDIO_ID, connection);
            assertThat(result)
                    .isPresent()
                    .get()
                    .satisfies(studio -> assertThat(studio.getName()).isEqualTo(STUDIO_NAME));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findAll() {
        try (Connection connection = ConnectionPool.get()) {
            List<Studio> result = studioDao.findAll(connection);
            assertThat(result).hasSize(SIZE_ALL_STUDIOS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void save() {
        try (Connection connection = ConnectionPool.get()) {
            Studio result = studioDao.save(new Studio(
                    0L,
                    "test",
                    LocalDate.now()
            ), connection);

            assertThat(result.getName()).isEqualTo("test");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
        try (Connection connection = ConnectionPool.get()) {
            Optional<Studio> testStudio = studioDao.findById(STUDIO_ID, connection);
            testStudio.get().setName(STUDIO_NAME + "!");

            studioDao.update(testStudio.get(), connection);

            Optional<Studio> result = studioDao.findById(STUDIO_ID, connection);
            assertThat(result)
                    .isPresent()
                    .get()
                    .satisfies(studio -> assertThat(studio.getName()).isEqualTo(STUDIO_NAME + "!"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete() {
        try (Connection connection = ConnectionPool.get()) {
            boolean result = studioDao.delete(STUDIO_ID, connection);
            List<Studio> listResult = studioDao.findAll(connection);

            assertThat(result).isTrue();
            assertThat(listResult).hasSize(SIZE_ALL_STUDIOS - 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}