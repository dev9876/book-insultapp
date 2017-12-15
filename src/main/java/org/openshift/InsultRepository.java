package org.openshift;

import org.openshift.MoreFunctions.TriFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsultRepository {

    private static final String DATABASE_URL = "jdbc:postgresql://"
            + System.getenv("POSTGRESQL_SERVICE_HOST")
            + "/" + System.getenv("POSTGRESQL_DATABASE");
    private static final String USERNAME = System.getenv("POSTGRESQL_USER");
    private static final String PASSWORD = System.getenv("PGPASSWORD");

    private static final String GET_INSULT_SQL = "select " +
            "a.string as first, " +
            "b.string as second, " +
            "c.string as noun " +
            "from short_adjective a, " +
            "long_adjective b, " +
            "noun c " +
            "order by random () " +
            "limit 1";

    public String getInsult(final TriFunction<String, String> function, final String defaultValue) {
        try {
            return getObject(GET_INSULT_SQL, rs -> function.apply(
                    rs.getString("first"),
                    rs.getString("second"),
                    rs.getString("noun")));
        } catch (SQLException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    private <T> T getObject(final String sql, final RowMapper<T> rowMapper) throws SQLException {
        try (final Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            if (connection != null) {
                try (final ResultSet rs = connection.createStatement().executeQuery(sql)) {
                    if (rs.next()) {
                        final T result = rowMapper.map(rs);
                        if (rs.next()) {
                            throw new SQLException("Too many results found");
                        }
                        return result;
                    } else {
                        throw new SQLException("No results found");
                    }
                }
            } else {
                throw new SQLException("Cannot connect to database");
            }
        }
    }

    @FunctionalInterface
    private interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}