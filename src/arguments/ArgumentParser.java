package arguments;

import java.util.*;
import java.util.regex.Pattern;

public class ArgumentParser {
    final private Map<String, ArgumentPattern> required = new HashMap<>();
    final private Map<String, ArgumentPattern> either = new HashMap<>();

    public ArgumentParser(ArgumentPattern[] required, ArgumentPattern[] either) {
        for (ArgumentPattern pattern : required) {
            setArgumentPattern(pattern, true);
        }

        for (ArgumentPattern pattern : either) {
            setArgumentPattern(pattern, false);
        }
    }

    String getArgumentDescription(Collection<ArgumentPattern> patterns) {
        return String.join("\n", patterns
                .stream()
                .map(pattern -> String.format("--%s<%s>", pattern.key, pattern.valueDescription))
                .toList()
        );
    }

    String getArgumentDescription() {
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

    public void setArgumentPattern(ArgumentPattern pattern, boolean required) {
        if (required) {
            this.required.put(pattern.key, pattern);
        } else {
            either.put(pattern.key, pattern);
        }
    }

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
                    "The following required arguments where missing:%n%s%n",
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
