package com.qirsam.database.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface CrudDao<K, E> {

    Optional<E> findById(K id, Connection connection);

    List<E> findAll(Connection connection);

    E save(E entity, Connection connection);

    void update(E entity, Connection connection);
    boolean delete(K id, Connection connection);
}
