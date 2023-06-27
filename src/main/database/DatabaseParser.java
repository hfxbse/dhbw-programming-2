package main.database;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

public class DatabaseParser {
    /**
     * Parses the database file and provides the values to an entry registration as array of Strings.
     * The matching entry registration is found by the pattern described in the "New_Entry"-line of the database.
     * If no entry registration matches the given entry pattern, those entries getting skipped.
     *
     * @param database Database file
     * @param entryRegistrations Map of entry patterns mapped to their matching entry registration.
     * @throws IOException If the database file could not be read for any reason.
     */
    static public void parse(File database, Map<String, EntryRegistration> entryRegistrations) throws IOException {
        if (!database.canRead()) throw new IOException("Permission denied to read main.database file.");

        BufferedReader input = new BufferedReader(new FileReader(database));

        EntryRegistration entryRegistration = null;
        String line;
        while ((line = input.readLine()) != null) {
            if (line.startsWith("New_Entity")) {
                entryRegistration = entryRegistrations.get(line.substring(line.indexOf(":") + 1).trim());
                continue;
            }

            if (entryRegistration == null) {
                continue;
            }

            entryRegistration.registerEntry(
                    Arrays.stream(line.split("(\"\")*,"))
                            .map(value -> value.replaceAll("(^ *\"* *| *\"* *$)", ""))
                            .toList()
                            .toArray(new String[0])
            );
        }
    }
}
