package com.qirsam;

import com.qirsam.database.dao.ActorDao;
import com.qirsam.utils.ConnectionPool;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Connection connection = ConnectionPool.get();
        ActorDao.getInstance().findById(1L, connection);
    }
}