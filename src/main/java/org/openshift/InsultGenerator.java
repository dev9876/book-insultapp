package org.openshift;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InsultGenerator {

    private interface TriFunction<T> {
        T accept(String firstAdjective, String secondAdjective, String noun);
    }

    private static final String DATABASE_URL = "jdbc:postgresql://"
            + System.getenv("POSTGRESQL_SERVICE_HOST")
            + "/" + System.getenv("POSTGRESQL_DATABASE");
    private static final String USERNAME = System.getenv("POSTGRESQL_USER");
    private static final String PASSWORD = System.getenv("PGPASSWORD");

    @SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
    private static final String SQL = "select " +
            "a.string AS first, " +
            "b.string AS second, " +
            "c.string AS noun " +
            "from short_adjective a, " +
            "long_adjective b, " +
            "noun c " +
            "ORDER BY random () " +
            "limit 1";

    private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList('A', 'E', 'I', 'O', 'U'));
    private static final String INSULT_TEMPLATE = "Thou art %s %s %s %s!";

    public String generateInsult() {
        return getInsult((firstAdjective, secondAdjective, noun) ->
                String.format(INSULT_TEMPLATE, articleFor(firstAdjective), firstAdjective, secondAdjective, noun));
    }

    private static String articleFor(final String adjective) {
        return VOWELS.contains(adjective.charAt(0)) ? "an" : "a";
    }

    private String getInsult(TriFunction<String> function) {
        String result = "I am lost for words!";
        Connection connection = null;
        ResultSet rs = null;
        try {
            if ((connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) != null) {
                rs = connection.createStatement().executeQuery(SQL);
                if (rs.next()) {
                    result = function.accept(rs.getString("first"), rs.getString("second"), rs.getString("noun"));
                }
            }
            return result;
        } catch (Exception e) {
            return "Database connection problem!";
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
