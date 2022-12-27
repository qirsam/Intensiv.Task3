package com.qirsam;

import com.qirsam.database.dao.ActorDao;
import com.qirsam.database.entity.Actor;
import com.qirsam.utils.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Connection connection = ConnectionPool.get();
        ActorDao.getInstance().save(new Actor(10L, "Jared", "Leto", LocalDate.now(), "male"), connection);


    }
}