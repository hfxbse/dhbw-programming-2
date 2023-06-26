package test.arguments;

import main.arguments.ArgumentParser;
import main.arguments.ArgumentPattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ArgumentParserTest {
    final static ArgumentParser parser = new ArgumentParser(
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
        final Map<String, String> firstOption = parser
                .parse(new String[]{"--required1=first", "--required2=2", "--either1=third"});

        final Map<String, String> secondOption = parser
                .parse(new String[]{"--required1=first", "--required2=2", "--either2=3"});

        assertAll(
                "Key value map assertion option 1.",
                () -> assertEquals("first", firstOption.get("required1")),
                () -> assertEquals("2", firstOption.get("required2")),
                () -> assertEquals("third", firstOption.get("either1")),
                () -> assertNull(firstOption.get("either2"))
        );
        assertAll(
                "Key value map assertion option 2.",
                () -> assertEquals("first", secondOption.get("required1")),
                () -> assertEquals("2", secondOption.get("required2")),
                () -> assertEquals("3", secondOption.get("either2")),
                () -> assertNull(secondOption.get("either1"))
        );
    }
}
