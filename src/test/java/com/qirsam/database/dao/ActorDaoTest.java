package com.qirsam.database.dao;

import com.qirsam.IntegrationTestBase;
import com.qirsam.database.entity.Actor;
import com.qirsam.utils.ConnectionPool;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActorDaoTest extends IntegrationTestBase {

    @Test
    void findById() {
        Connection connection = ConnectionPool.get  ();
        Optional<Actor> mayBeResult = ActorDao.getInstance().findById(1L, connection);

        org.junit.jupiter.api.Assertions.assertTrue(mayBeResult.isPresent());
        mayBeResult.ifPresent(actor -> assertEquals(actor.getFirstname(), "Tom"));
    }

    @Test
    void findAll() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}