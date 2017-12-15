package org.openshift;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

import static java.lang.String.format;

public class InsultGenerator {

    private static final Set<Character> VOWELS = ImmutableSet.of('A', 'E', 'I', 'O', 'U');
    private static final String INSULT_TEMPLATE = "Thou art %s %s, %s %s!";
    private final InsultRepository insultRepository;

    InsultGenerator(final InsultRepository insultRepository) {
        this.insultRepository = insultRepository;
    }

    public String generateInsult() {
        return insultRepository.getInsult((firstAdjective, secondAdjective, noun) ->
                        format(INSULT_TEMPLATE, articleFor(firstAdjective), firstAdjective, secondAdjective, noun),
                "Thou art... words fail me!");
    }

    private static String articleFor(final String adjective) {
        Preconditions.checkArgument(adjective != null && !adjective.isEmpty());
        final boolean startsWithVowel = VOWELS.contains(Character.toUpperCase(adjective.charAt(0)));
        return startsWithVowel ? "an" : "a";
    }

}
