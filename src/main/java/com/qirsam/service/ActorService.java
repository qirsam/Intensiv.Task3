package com.qirsam.service;

import com.qirsam.database.dao.ActorDao;
import com.qirsam.database.entity.Actor;
import com.qirsam.database.entity.Film;
import com.qirsam.utils.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ActorService {
    private static final ActorService INSTANCE = new ActorService();
    private static final ActorDao actorDao = ActorDao.getInstance();

    private ActorService(){}

    public static ActorService getInstance(){
        return INSTANCE;
    }

    public Actor findById(Long id) {
        try (Connection connection = ConnectionPool.get()) {
            return actorDao.findById(id, connection).orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Actor> findAll() {
        try (Connection connection = ConnectionPool.get()) {
            return actorDao.findAll(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Actor save(Actor actor) {
        try (Connection connection = ConnectionPool.get()) {
            return actorDao.save(actor, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Actor actor) {
        try (Connection connection = ConnectionPool.get()) {
            actorDao.update(actor, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Long id) {
        try (Connection connection = ConnectionPool.get()) {
            return actorDao.delete(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Film> findFilmsByActorId(Long id) {
        try (Connection connection = ConnectionPool.get()) {
            return actorDao.findFilmsByActorId(id, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
