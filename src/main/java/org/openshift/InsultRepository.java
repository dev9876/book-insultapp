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

    @SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
    private static final String SQL = "select " +
            "a.string as first, " +
            "b.string as second, " +
            "c.string as noun " +
            "from short_adjective a, " +
            "long_adjective b, " +
            "noun c " +
            "order by random () " +
            "limit 1";

    public String getInsult(final TriFunction<String, String> function, final String defaultValue) {
        String result = defaultValue;
        Connection connection = null;
        ResultSet rs = null;
        try {
            if ((connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) != null) {
                rs = connection.createStatement().executeQuery(SQL);
                if (rs.next()) {
                    result = function.apply(
                            rs.getString("first"),
                            rs.getString("second"),
                            rs.getString("noun"));
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }

}
