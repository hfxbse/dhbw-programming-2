package main.arguments;

public class ArgumentPattern {
    String key;
    String valueDescription;
    boolean numeric;

    public ArgumentPattern(String key, String valueDescription) {
        this(key, valueDescription, false);
    }

    public ArgumentPattern(String key, String valueDescription, boolean numeric) {
        this.key = key;
        this.valueDescription = valueDescription;
        this.numeric = numeric;
    }
}
