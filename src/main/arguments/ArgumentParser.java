package main.arguments;

import java.util.*;
import java.util.regex.Pattern;

public class ArgumentParser {
    final private Map<String, ArgumentPattern> required = new HashMap<>();
    final private Map<String, ArgumentPattern> either = new HashMap<>();

    /**
     * Constructs a new argument parser.
     *
     * @param required List of argument patterns that need to be present to be considered valid.
     * @param either   List of argument patterns, of which exactly one needs to be present to be considered valid.
     */
    public ArgumentParser(ArgumentPattern[] required, ArgumentPattern[] either) {
        for (ArgumentPattern pattern : required) {
            setArgumentPattern(pattern, true);
        }

        for (ArgumentPattern pattern : either) {
            setArgumentPattern(pattern, false);
        }
    }

    /**
     * Creates the help text for the given argument patterns.
     *
     * @param patterns List of arguments from which to generate the help text.
     * @return The generated help text.
     */
    public String getArgumentDescription(Collection<ArgumentPattern> patterns) {
        return String.join("\n", patterns
                .stream()
                .map(pattern -> String.format("--%s=<%s>", pattern.key, pattern.valueDescription))
                .toList()
        );
    }

    /**
     * Create the help text for the current argument patterns.
     *
     * @return The generated help text.
     */
    public String getArgumentDescription() {
        String description = "";

        if (!required.isEmpty()) {
            description += String.format("Required parameters:%n%s", getArgumentDescription(required.values()));
        }

        if (!either.isEmpty()) {
            if (!description.isEmpty()) description += "\n\n";

            description += String.format(
                    "In addition, add exactly one of those parameters:%n%s",
                    getArgumentDescription(either.values())
            );
        }

        return description;
    }

    /**
     * Adds a new argument pattern to the parser.
     * If the argument key is already present, it will override the previous pattern configuration.
     *
     * @param pattern  The argument pattern to be added or overridden.
     * @param required If the argument pattern is required or not.
     */
    public void setArgumentPattern(ArgumentPattern pattern, boolean required) {
        if (required) {
            this.required.put(pattern.key, pattern);
        } else {
            either.put(pattern.key, pattern);
        }
    }

    /**
     * Parses the giving strings to a key-value map, in which the key is the key of the argument pattern and the
     * matching value the argument value as trimmed text.
     *
     * @param args List of parameters as string.
     * @return Key-Value map of the parameters if the parameter configuration was valid, else null.
     */
    public Map<String, String> parse(String[] args) {
        final int count = required.size() + 1;

        if (args.length != count) {
            System.err.printf(
                    "Expected exactly %d argument%s.%n%n%s%n",
                    count, count > 1 ? "s" : "", getArgumentDescription()
            );
            return null;
        }

        final Pattern generalPattern = Pattern.compile("--.+=.+");
        final Map<String, String> parameters = new HashMap<>();

        final Set<String> missingKeys = new HashSet<>(required.keySet());
        final Set<String> validKeys = new HashSet<>(required.keySet());
        validKeys.addAll(either.keySet());

        for (String arg : args) {
            if (!generalPattern.matcher(arg).matches() || !validKeys.contains(getArgumentKey(arg))) {
                System.err.printf("Unexpected argument: %s%n%n%s%n", arg, getArgumentDescription());
                return null;
            }

            final String key = getArgumentKey(arg);
            final ArgumentPattern pattern = required.get(key) != null ? required.get(key) : either.get(key);
            final String parameter = getArgumentValue(arg);

            if (pattern.numeric && !parameter.matches("^\\d+$")) {
                System.err.printf(
                        "Argument %s expected a number as value but found \"%s\".%n%n%s%n",
                        key, parameter, getArgumentDescription()
                );
                return null;
            }

            missingKeys.remove(key);
            parameters.put(key, parameter);
        }

        if (!missingKeys.isEmpty()) {
            System.err.printf(
                    "The following required main.arguments where missing:%n%s%n",
                    getArgumentDescription(required
                            .keySet()
                            .stream()
                            .filter(missingKeys::contains)
                            .map(required::get).toList()
                    )
            );

            return null;
        }

        return parameters;
    }

    private String getArgumentKey(String argument) {
        return argument.substring(2, argument.indexOf('='));
    }

    private String getArgumentValue(String argument) {
        return argument.substring(argument.indexOf("=") + 1).trim();
    }
}
