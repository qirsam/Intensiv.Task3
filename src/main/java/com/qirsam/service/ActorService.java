package com.qirsam.service;

public class ActorService {
    private static final ActorService INSTANCE = new ActorService();

    private ActorService(){}

    public static ActorService getInstance(){
        return INSTANCE;
    }
}
