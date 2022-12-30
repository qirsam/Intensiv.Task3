package com.qirsam;

import com.qirsam.database.dao.ActorDao;
import com.qirsam.database.dao.ActorFilmDao;
import com.qirsam.database.dao.StudioDao;
import com.qirsam.database.entity.Actor;
import com.qirsam.database.entity.ActorFilm;
import com.qirsam.database.entity.Studio;
import com.qirsam.utils.ConnectionPool;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Connection connection = ConnectionPool.get();
        Optional<Actor> byId1 = ActorDao.getInstance().findById(4L, connection);
        List<ActorFilm> byActorId = ActorFilmDao.getInstance().findByActorId(byId1.get().getId(), connection);
        System.out.println();
    }
}