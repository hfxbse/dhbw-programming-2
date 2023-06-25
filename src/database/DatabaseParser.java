package database;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

public class DatabaseParser {
    static public void parse(File database, Map<String, EntryRegistration> entryParsers) throws IOException {
        if (!database.canRead()) throw new IOException("Permission denied to read database file.");

        BufferedReader input = new BufferedReader(new FileReader(database));

        EntryRegistration entryRepository = null;
        String line;
        while ((line = input.readLine()) != null) {
            if (line.startsWith("New_Entity")) {
                entryRepository = entryParsers.get(line.substring(line.indexOf(":") + 1).trim());
                continue;
            }

            if (entryRepository == null) {
                continue;
            }

            entryRepository.registerEntry(
                    Arrays.stream(line.split("(\"\")*,"))
                            .map(value -> value.replaceAll("(^ *\"* *| *\"* *$)", ""))
                            .toList()
                            .toArray(new String[0])
            );
        }
    }
}
