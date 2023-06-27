package main.database.people;

import main.database.EntryRegistration;

public class FriendshipRegistration implements EntryRegistration {
    public static final String DATABASE_ENTRY_PATTERN = "\"person1_id\",\"person2_id\"";

    public PeopleRepository people;

    public FriendshipRegistration(PeopleRepository people) {
        this.people = people;
    }

    /**
     * Adds a friend to both person in the friendship.
     *
     * @param data Two person IDs, one for each friend, list of strings.
     */
    @Override
    public void registerEntry(String[] data) {
        Integer first = Integer.parseInt(data[0]);
        Integer second = Integer.parseInt(data[1]);

        try {
            people.getById(first).friends.add(people.getById(second));
            people.getById(second).friends.add(people.getById(first));
        } catch (NullPointerException e) {
            throw new RuntimeException("At least one person in friendship does not exist at this point.", e);
        }
    }
}
