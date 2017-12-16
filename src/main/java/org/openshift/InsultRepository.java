package org.openshift;

import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class InsultRepository {

    private final static Logger LOGGER = Logger.getLogger(InsultRepository.class.getName());

    private static final String GET_INSULT_SQL = "select " +
            "a.string as first, " +
            "b.string as second, " +
            "c.string as noun " +
            "from short_adjective a, " +
            "long_adjective b, " +
            "noun c " +
            "order by random () " +
            "limit 1";

    private final JdbcTemplate jdbcTemplate;

    InsultRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getInsult(final InsultMapper insultMapper, final String defaultValue) {
        try {
            return jdbcTemplate.getObject(GET_INSULT_SQL, rs -> insultMapper.map(
                    rs.getString("first"),
                    rs.getString("second"),
                    rs.getString("noun")));
        } catch (Exception e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return defaultValue;
        }
    }

    @FunctionalInterface
    public interface InsultMapper {
        String map(String firstAdjective, String secondAdjective, String noun);
    }

}