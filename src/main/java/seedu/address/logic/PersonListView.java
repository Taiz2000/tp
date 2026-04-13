package seedu.address.logic;

/**
 * Describes which list the user sees or should see.
 */
public enum PersonListView {
    KEPT_PERSONS,
    DELETED_PERSONS;

    public String getDisplayMessage() {
        switch (this) {
        case KEPT_PERSONS:
            return "You are viewing your contact list of active volunteers.";
        case DELETED_PERSONS:
            return "You are viewing your recycle bin of recently deleted volunteers.";
        default:
            throw new IllegalStateException("Unexpected value of personListView: " + this);
        }
    }
}
