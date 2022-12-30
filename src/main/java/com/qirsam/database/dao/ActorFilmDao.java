package com.qirsam.database.dao;

import com.qirsam.database.entity.ActorFilm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActorFilmDao implements CrudDao<Long, ActorFilm> {

    private static final ActorFilmDao INSTANCE = new ActorFilmDao();

    private static final ActorDao actorDao = ActorDao.getInstance();

    private static final FilmDao filmDao = FilmDao.getInstance();
    private static final String FIND_ALL_SQL = """
            SELECT actor_film.id,
            actor_id,
            film_id
            FROM actor_film
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?;
            """;

    private static final String SAVE_SQL = """
            INSERT INTO actor_film(actor_id, film_id) 
            VALUES (?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE actor_film
            SET actor_id = ?,
                film_id = ?
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM actor_film
            WHERE id = ?
            """;


    private ActorFilmDao() {
    }

    public static ActorFilmDao getInstance() {
        return INSTANCE;
    }

    @Override
    public List<ActorFilm> findAll(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ActorFilm> actorFilms = new ArrayList<>();

            while (resultSet.next()) {
                actorFilms.add(buildActorFilm(resultSet));
            }
            return actorFilms;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ActorFilm> findById(Long id, Connection connection) {
        return Optional.empty();
    }

    @Override
    public ActorFilm save(ActorFilm entity, Connection connection) {
        return null;
    }

    @Override
    public void update(ActorFilm entity, Connection connection) {

    }

    @Override
    public boolean delete(Long id, Connection connection) {
        return false;
    }

    private ActorFilm buildActorFilm(ResultSet resultSet) throws SQLException {
        Connection connection = resultSet.getStatement().getConnection();
        return new ActorFilm(
                resultSet.getLong("id"),
                actorDao.findById(resultSet.getLong("actor_id"),
                        connection).orElse(null),
                filmDao.findById(resultSet.getLong("film_id"),
                        connection).orElse(null)
        );
    }
}
