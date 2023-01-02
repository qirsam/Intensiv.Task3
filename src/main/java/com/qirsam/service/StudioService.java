package com.qirsam.service;

import com.qirsam.database.dao.StudioDao;
import com.qirsam.database.entity.Studio;
import com.qirsam.utils.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StudioService {

    private static final StudioService INSTANCE = new StudioService();

    private StudioService(){}

    private static final StudioDao studioDao = StudioDao.getInstance();

    public static StudioService getInstance(){
        return INSTANCE;
    }

    public List<Studio> findAll(){
        try (Connection connection = ConnectionPool.get()) {
            List<Studio> studios = studioDao.findAll(connection);
            return studios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Studio findById(Long id) {
        try (Connection connection = ConnectionPool.get()) {
            Optional<Studio> mayBeStudio = studioDao.findById(id, connection);
            return mayBeStudio.orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Studio save(Studio studio) {
        try (Connection connection = ConnectionPool.get()) {
            Studio savedStudio = studioDao.save(studio, connection);
            return savedStudio;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
