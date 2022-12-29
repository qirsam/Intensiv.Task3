package com.qirsam;

import com.qirsam.database.dao.StudioDao;
import com.qirsam.database.entity.Studio;
import com.qirsam.utils.ConnectionPool;

import java.sql.Connection;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Connection connection = ConnectionPool.get();
        Optional<Studio> byId = StudioDao.getInstance().findById(1L, connection);
        System.out.println();
    }
}