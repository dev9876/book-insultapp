package org.openshift;

import com.google.common.base.Preconditions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {

    private final String databaseUrl;
    private final String username;
    private final String password;

    JdbcTemplate(final String databaseUrl, final String username, final String password) {
        this.databaseUrl = databaseUrl;
        this.username = username;
        this.password = password;
    }

    public <T> T getObject(final String sql, final RowMapper<T> rowMapper) throws RuntimeException, SQLException {
        try (final Connection connection = DriverManager.getConnection(databaseUrl, username, password)) {
            Preconditions.checkNotNull(connection);
            try (final ResultSet rs = connection.createStatement().executeQuery(sql)) {
                Preconditions.checkState(rs.next());
                Preconditions.checkState(rs.isLast());
                return rowMapper.map(rs);
            }
        }
    }

    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}
