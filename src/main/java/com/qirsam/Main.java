package com.qirsam;

import com.qirsam.database.entity.Studio;
import com.qirsam.service.StudioService;
import com.qirsam.utils.ConnectionPool;

import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Connection connection = ConnectionPool.get();
        List<Studio> all = StudioService.getInstance().findAll();
        System.out.println();
    }
}