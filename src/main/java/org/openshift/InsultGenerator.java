package org.openshift;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class InsultGenerator {

    private final InsultRepository insultRepository;

    public InsultGenerator() {
        this.insultRepository = new InsultRepository();
    }

    private static final Set<Character> VOWELS = ImmutableSet.of('A', 'E', 'I', 'O', 'U');
    private static final String INSULT_TEMPLATE = "Thou art %s %s, %s %s!";

    public String generateInsult() {
        return insultRepository.getInsult((firstAdjective, secondAdjective, noun) ->
                        String.format(INSULT_TEMPLATE, articleFor(firstAdjective), firstAdjective, secondAdjective, noun),
                "Thou art... words fail me!");
    }

    private static String articleFor(final String adjective) {
        Preconditions.checkArgument(adjective != null && !adjective.isEmpty());
        return VOWELS.contains(Character.toUpperCase(adjective.charAt(0))) ? "an" : "a";
    }

}
