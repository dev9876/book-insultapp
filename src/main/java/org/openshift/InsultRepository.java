package org.openshift;

import com.google.common.base.Preconditions;

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

    public String getInsult(final InsultMapper insultMapper, final String defaultValue) {
        try {
            return getObject(GET_INSULT_SQL, rs -> insultMapper.map(
                    rs.getString("first"),
                    rs.getString("second"),
                    rs.getString("noun")));
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    private <T> T getObject(final String sql, final RowMapper<T> rowMapper) throws RuntimeException, SQLException {
        try (final Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            Preconditions.checkNotNull(connection);
            try (final ResultSet rs = connection.createStatement().executeQuery(sql)) {
                Preconditions.checkState(rs.next());
                Preconditions.checkState(rs.isLast());
                return rowMapper.map(rs);
            }
        }
    }

    @FunctionalInterface
    public interface InsultMapper {
        String map(String firstAdjective, String secondAdjective, String noun);
    }

    @FunctionalInterface
    private interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}