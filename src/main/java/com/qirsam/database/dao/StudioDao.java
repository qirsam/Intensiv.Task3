package com.qirsam.database.dao;

import com.qirsam.database.entity.Studio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StudioDao implements CrudDao<Long, Studio> {

    private static final StudioDao INSTANCE = new StudioDao();

    private static final String FIND_BY_ID_SQL = """
            SELECT id,
            name,
            date_of_foundation
            FROM studio
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
    private static final String FIND_ALL_SQL = """
            SELECT studio.id,
            name,
            date_of_foundation
            FROM studio
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
        return null;
    }

    @Override
    public Studio save(Studio entity, Connection connection) {
        return null;
    }

    @Override
    public void update(Studio entity, Connection connection) {

    }

    @Override
    public boolean delete(Long id, Connection connection) {
        return false;
    }

    private Studio buildStudio(ResultSet resultSet) throws SQLException {
        Studio studio = new Studio(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date_of_foundation").toLocalDate()
        );
        return studio;
    }
}
