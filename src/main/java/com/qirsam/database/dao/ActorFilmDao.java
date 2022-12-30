package com.qirsam.database.dao;

import com.qirsam.database.entity.ActorFilm;

import java.sql.*;
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

    private static final String FIND_BY_ACTOR_ID_SQL = FIND_ALL_SQL + """
            WHERE actor_id = ?
            """;

    private static final String FIND_BY_FILM_ID_SQL = FIND_ALL_SQL + """
            WHERE film_id = ?
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            ActorFilm actorFilm = null;
            if (resultSet.next()){
                actorFilm = buildActorFilm(resultSet);
            }
            return Optional.ofNullable(actorFilm);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ActorFilm save(ActorFilm entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, entity.getActor().getId());
            preparedStatement.setLong(2, entity.getFilm().getId());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong("id"));
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(ActorFilm entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setLong(1, entity.getActor().getId());
            preparedStatement.setLong(2, entity.getFilm().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);

            return  preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ActorFilm> findByActorId(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ACTOR_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<ActorFilm> actorFilms = new ArrayList<>();
            while (resultSet.next()){
                actorFilms.add(buildActorFilm(resultSet));
            }
            return actorFilms;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ActorFilm> findByFilmId(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_FILM_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<ActorFilm> actorFilms = new ArrayList<>();
            while (resultSet.next()){
                actorFilms.add(buildActorFilm(resultSet));
            }
            return actorFilms;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
