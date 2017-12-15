package org.openshift;

public class InsultRepository {

    private static final String GET_INSULT_SQL = "select " +
            "a.string as first, " +
            "b.string as second, " +
            "c.string as noun " +
            "from short_adjective a, " +
            "long_adjective b, " +
            "noun c " +
            "order by random () " +
            "limit 1";

    private final RepositoryTemplate template;

    InsultRepository(final RepositoryTemplate template) {
        this.template = template;
    }

    public String getInsult(final InsultMapper insultMapper, final String defaultValue) {
        try {
            return template.getObject(GET_INSULT_SQL, rs -> insultMapper.map(
                    rs.getString("first"),
                    rs.getString("second"),
                    rs.getString("noun")));
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    @FunctionalInterface
    public interface InsultMapper {
        String map(String firstAdjective, String secondAdjective, String noun);
    }

}