package com.qirsam.database.dao;

import com.qirsam.database.entity.Studio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudioDao implements CrudDao<Long, Studio> {

    private static final StudioDao INSTANCE = new StudioDao();

    private static final String FIND_ALL_SQL = """
            SELECT studio.id,
                   name,
                   date_of_foundation
            FROM studio
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO studio (name, date_of_foundation) 
            VALUES (?,?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE studio
            SET name = ?,
                date_of_foundation = ?
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM studio
            WHERE id = ?
            """;

    private StudioDao() {
    }

    public static StudioDao getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Studio> findById(Long id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            Studio studio = null;
            if (resultSet.next()) {
                studio = buildStudio(resultSet);
            }
            return Optional.ofNullable(studio);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Studio> findAll(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Studio> studios = new ArrayList<>();

            while (resultSet.next()) {
                studios.add(buildStudio(resultSet));
            }
            return studios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Studio save(Studio entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setDate(2, Date.valueOf(entity.getDateOfFoundation()));

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (preparedStatement.getGeneratedKeys().next()) {
                entity.setId(generatedKeys.getLong("id"));
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Studio entity, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setDate(2, Date.valueOf(entity.getDateOfFoundation()));

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

    private Studio buildStudio(ResultSet resultSet) throws SQLException {
        return new Studio(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date_of_foundation").toLocalDate()
        );
    }
}
