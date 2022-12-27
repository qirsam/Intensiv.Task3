package com.qirsam.database.dao;

import com.qirsam.database.entity.Actor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ActorDao implements CrudDao<Long, Actor> {

    private static final ActorDao INSTANCE = new ActorDao();
    private static final String FIND_BY_ID_SQL = """
            SELECT id,
            firstname,
            lastname,
            birthdate,
            sex
            FROM actor
            WHERE id = ?
            """;

    private ActorDao() {
    }

    public static ActorDao getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Actor> findById(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            Actor actor = null;
            if (resultSet.next()) {
                actor = buildActor(resultSet);
            }
            return Optional.ofNullable(actor);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Actor> findAll(Connection connection) {
        return null;
    }

    @Override
    public Actor save(Actor entity, Connection connection) {
        return null;
    }

    @Override
    public void update(Actor entity, Connection connection) {

    }

    @Override
    public boolean delete(Long id, Connection connection) {
        return false;
    }

    private Actor buildActor(ResultSet resultSet) throws SQLException {
        return new Actor(
                resultSet.getLong("id"),
                resultSet.getString("firstname"),
                resultSet.getString("lastname"),
                resultSet.getDate("birthdate").toLocalDate(),
                resultSet.getString("sex")
        );
    }
}
