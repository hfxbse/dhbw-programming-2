package main.arguments;

/**
 * Describes an allowed command line argument.
 * The value description is used for the help text.
 * If numeric is true, only a whole number is considered a valid parameter value.
 */
public class ArgumentPattern {
    public String key;
    public String valueDescription;
    public boolean numeric;

    public ArgumentPattern(String key, String valueDescription) {
        this(key, valueDescription, false);
    }

    public ArgumentPattern(String key, String valueDescription, boolean numeric) {
        this.key = key;
        this.valueDescription = valueDescription;
        this.numeric = numeric;
    }
}
