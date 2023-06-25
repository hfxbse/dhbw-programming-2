import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ArgumentValidator {
    private final Map<String, Pattern> patterns = new HashMap<>();

    public ArgumentValidator(String[] patterns) {
        for (String pattern : patterns) {
            addPattern(pattern);
        }
    }

    public void addPattern(String pattern) {
        if (patterns.containsKey(pattern)) return;

        patterns.put(pattern, Pattern.compile(pattern));
    }

    public String findPattern(String argument) {
        for (Map.Entry<String, Pattern> pattern : patterns.entrySet()) {
            if (pattern.getValue().matcher(argument).matches()) return pattern.getKey();
        }

        return null;
    }
}
