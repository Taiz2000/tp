package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {

            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@rosterbolt.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), new Role("Mentor"),
                new Notes("Available on weekday evenings."), getTagSet("ops", "mentor")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@rosterbolt.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),new Role("Logistics Coordinator"),
                new Notes("Has van access for equipment runs."), getTagSet("logistics", "driver")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@rosterbolt.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),new Role("First Aider"),
                new Notes("Certified in basic first aid."), getTagSet("medical", "firstaid")),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@rosterbolt.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), new Role("Admin Helper"),
                new Notes("Fast at front-desk check-in."), getTagSet("admin", "weekend")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"),new Role("Site Setup Crew"),
                new Notes("Can commit to early morning setup."), getTagSet("setup", "ops")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), new Role("Outreach & Publicity"),
                new Notes("Prefers community booth assignments."), getTagSet("outreach", "weekend")),
            new Person(new Name("Ng Fo Toh"), new Phone("96780123"), new Email("fotoh.ng@rosterbolt.org"),
                new Address("6 Hougang Avenue 7, #67-67"), new Role("Media & Photography"),
                new Notes("Handles event photos and recap posts."), getTagSet("media", "photography")),
            new Person(new Name("Varun Teer"), new Phone("97891234"), new Email("varun.teer@rosterbolt.org"),
                new Address("288 Bukit Batok Street 58, #04-67"), new Role("Volunteer"),
                new Notes("Can cover last-minute roster gaps."), getTagSet("backup", "flex"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
