package com.qirsam.database.dao;

import com.qirsam.database.entity.Actor;
import com.qirsam.database.entity.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FilmDao implements CrudDao<Long, Film> {

    private static final FilmDao INSTANCE = new FilmDao();
    private static final StudioDao studioDao = StudioDao.getInstance();

    private static final String FIND_ALL_SQL = """
            SELECT film.id,
                   name,
                   date_release,
                   studio_id
            FROM film
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO  film(name, date_release, studio_id) 
            VALUES (?,?,?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE film
            SET name = ?,
                date_release = ?,
                studio_id = ?
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM film
            WHERE id = ?
            """;
    private static final String FIND_ACTORS_BY_FILM_ID_SQL = """
            SELECT a.id, firstname, lastname, birthdate, sex
            FROM film
            LEFT JOIN actor_film af on film.id = af.film_id
            LEFT JOIN actor a on a.id = af.actor_id
            WHERE film_id = ?
            """;

    private FilmDao() {
    }

    public static FilmDao getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Film> findAll(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Film> films = new ArrayList<>();

            while (resultSet.next()) {
                films.add(buildFilm(resultSet));
            }
            return films;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Film> findById(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            Film film = null;
            if (resultSet.next()) {
                film = buildFilm(resultSet);
                film.setActors(findActorsByFilmId(id, connection));
            }
            return Optional.ofNullable(film);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Film save(Film entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setDate(2, Date.valueOf(entity.getDateRelease()));
            preparedStatement.setLong(3, entity.getStudio().getId());

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
    public void update(Film entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setDate(2, Date.valueOf(entity.getDateRelease()));
            preparedStatement.setLong(3, entity.getStudio().getId());
            preparedStatement.setLong(4, entity.getId());

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

    public List<Actor> findActorsByFilmId(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ACTORS_BY_FILM_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Actor> actors = new ArrayList<>();
            while (resultSet.next()) {
                actors.add(ActorDao.getInstance().buildActor(resultSet));
            }
            return actors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    protected Film buildFilm(ResultSet resultSet) throws SQLException {
        return new Film(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date_release").toLocalDate(),
                studioDao.findById(resultSet.getLong("studio_id"),
                        resultSet.getStatement().getConnection()).orElse(null));
    }
}
