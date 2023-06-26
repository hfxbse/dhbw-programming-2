package test;

import main.arguments.ArgumentParser;
import main.arguments.ArgumentPattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ArgumentParserTest {
    final ArgumentParser parser = new ArgumentParser(
            new ArgumentPattern[]{
                    new ArgumentPattern("required1", "required1"),
                    new ArgumentPattern("required2", "required2", true),
            },
            new ArgumentPattern[]{
                    new ArgumentPattern("either1", "required1"),
                    new ArgumentPattern("either2", "either2", true),
            }
    );

    @Test
    @DisplayName("Returns null if no arguments are provided")
    void noArguments() {
        assertNull(parser.parse(new String[0]));
    }

    @Test
    @DisplayName("Returns null if two few arguments are provided")
    void tooFewArguments() {
        assertNull(parser.parse(new String[]{"--first=first"}));
    }

    @Test
    @DisplayName("Returns null if two many arguments are provided")
    void tooManyArguments() {
        assertNull(parser.parse(new String[]{"--first=first", "--second=second", "--third=third", "--forth=forth"}));
    }

    @Test
    @DisplayName("Returns null if a required argument is missing")
    void requiredArgumentMissing() {
        assertNull(parser.parse(new String[]{"--required1=first", "--either1=second", "--either2=3"}));
    }

    @Test
    @DisplayName("Returns null if a argument is of wrong type")
    void wrongArgumentType() {
        assertNull(parser.parse(new String[]{"--required1=first", "--required2=wrong", "--either1=second"}));
    }

    @Test
    @DisplayName("Returns null if a argument key is invalid")
    void invalidArgumentKey() {
        assertNull(parser.parse(new String[]{"--invalid=first", "--required1=second", "--required2=3"}));
    }

    @Test
    @DisplayName("Returns key value map of parsed parameters")
    void validParametersMap() {
        Map<String, String> parameters = parser
                .parse(new String[]{"--required1=first", "--required2=2", "--either1=third"});

        assertEquals("first", parameters.get("required1"));
        assertEquals("2", parameters.get("required2"));
        assertEquals("third", parameters.get("either1"));
        assertNull(parameters.get("either2"));

        parameters = parser.parse(new String[]{"--required1=first", "--required2=2", "--either2=3"});
        assertEquals("first", parameters.get("required1"));
        assertEquals("2", parameters.get("required2"));
        assertEquals("3", parameters.get("either2"));
        assertNull(parameters.get("either1"));
    }
}
