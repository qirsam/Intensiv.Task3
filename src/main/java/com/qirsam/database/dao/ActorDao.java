package com.qirsam.database.dao;

import com.qirsam.database.entity.Actor;
import com.qirsam.database.entity.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActorDao implements CrudDao<Long, Actor> {

    private static final ActorDao INSTANCE = new ActorDao();
    private static final String FIND_ALL_SQL = """
            SELECT actor.id,
                firstname,
                lastname,
                birthdate,
                sex
            FROM actor
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO actor (firstname, lastname, birthdate, sex) 
            VALUES (?,?,?,?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE actor
            SET firstname = ?,
                lastname = ?,
                birthdate = ?,
                sex = ?
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM actor
            WHERE id = ?
            """;
    private static final String FIND_FILMS_BY_ACTOR_ID = """
            SELECT f.id, name, date_release, studio_id
            FROM actor
            LEFT JOIN actor_film af on actor.id = af.actor_id
            LEFT JOIN film f on f.id = af.film_id
            WHERE actor_id = ?
            """;

    private ActorDao() {
    }

    public static ActorDao getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Actor> findAll(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Actor> actors = new ArrayList<>();

            while (resultSet.next()) {
                actors.add(buildActor(resultSet));
            }
            return actors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Actor> findById(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            Actor actor = null;
            if (resultSet.next()) {
                actor = buildActor(resultSet);
                actor.setFilms(findFilmsByActorId(id, connection));
            }
            return Optional.ofNullable(actor);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Actor save(Actor entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getFirstname());
            preparedStatement.setString(2, entity.getLastname());
            preparedStatement.setDate(3, Date.valueOf(entity.getBirthDate()));
            preparedStatement.setString(4, entity.getSex());

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
    public void update(Actor entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getFirstname());
            preparedStatement.setString(2, entity.getLastname());
            preparedStatement.setDate(3, Date.valueOf(entity.getBirthDate()));
            preparedStatement.setString(4, entity.getSex());
            preparedStatement.setLong(5, entity.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean delete(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Film> findFilmsByActorId(Long id, Connection connection){
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_FILMS_BY_ACTOR_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Film> films = new ArrayList<>();
            while (resultSet.next()){
                films.add(FilmDao.getInstance().buildFilm(resultSet));
            }
            return films;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    protected Actor buildActor(ResultSet resultSet) throws SQLException {
        return new Actor(
                resultSet.getLong("id"),
                resultSet.getString("firstname"),
                resultSet.getString("lastname"),
                resultSet.getDate("birthdate").toLocalDate(),
                resultSet.getString("sex")
        );
    }
}
