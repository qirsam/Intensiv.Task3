package com.qirsam.service;

import com.qirsam.database.dao.FilmDao;
import com.qirsam.database.dao.StudioDao;
import com.qirsam.database.entity.Film;
import com.qirsam.database.entity.Studio;
import com.qirsam.utils.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class FilmService {
    private static final FilmService INSTANCE = new FilmService();
    private static final FilmDao filmDao = FilmDao.getInstance();
    private static final StudioDao studioDao = StudioDao.getInstance();

    private FilmService() {
    }

    public static FilmService getInstance() {
        return INSTANCE;
    }

    public Film findById(Long id) {
        try (Connection connection = ConnectionPool.get()) {
            return filmDao.findById(id, connection).orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Film> findAll() {
        try (Connection connection = ConnectionPool.get()) {
            return filmDao.findAll(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Film save(Film film) {
        try (Connection connection = ConnectionPool.get()) {
            Optional<Studio> mayBeStudio = studioDao.findById(film.getStudio().getId(), connection);
            // TODO: 02.01.2023  
            return filmDao.save(film, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void update(Film film) {
        try (Connection connection = ConnectionPool.get()) {
            filmDao.update(film, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
