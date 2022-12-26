package com.qirsam;

import com.qirsam.database.entity.utils.ConnectionPool;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ConnectionPool.get();
    }
}