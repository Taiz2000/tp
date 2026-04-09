package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_ROLE = new Prefix("r/");
    public static final Prefix PREFIX_NOTES = new Prefix("nt/");
    public static final Prefix PREFIX_AVAILABILITY = new Prefix("va/");
    public static final Prefix PREFIX_RECORD = new Prefix("vr/");
    public static final Prefix PREFIX_MATCH_TYPE = new Prefix("m/");
    public static final String KNOWN_PERSON_PREFIXES = "n/, p/, e/, a/, t/, r/, nt/, va/, vr/";
}
