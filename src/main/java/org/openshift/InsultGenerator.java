package org.openshift;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class InsultGenerator {

    private final InsultRepository insultRepository;

    public InsultGenerator() {
        this.insultRepository = new InsultRepository();
    }

    private static final Set<Character> VOWELS = Stream.of('A', 'E', 'I', 'O', 'U').collect(toSet());
    private static final String INSULT_TEMPLATE = "Thou art %s %s, %s %s!";

    public String generateInsult() {
        return insultRepository.getInsult((firstAdjective, secondAdjective, noun) ->
                        String.format(INSULT_TEMPLATE, articleFor(firstAdjective), firstAdjective, secondAdjective, noun),
                "Thou art... words fail me!");
    }

    private static String articleFor(final String adjective) {
        Objects.requireNonNull(adjective);
        return adjective.isEmpty()
                ? "nothing but"
                : VOWELS.contains(Character.toUpperCase(adjective.charAt(0))) ? "an" : "a";
    }

}
