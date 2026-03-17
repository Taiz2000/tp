package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * Reuse testing code from Codex.
 */
public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_returnsEightEntries() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        assertEquals(8, samplePersons.length);
    }

    @Test
    public void getSamplePersons_populatesRoleAndNotes() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        assertTrue(Arrays.stream(samplePersons)
                .allMatch(person -> !person.getRole().value.isBlank() && !person.getNotes().value.isBlank()));
    }

    @Test
    public void getSamplePersons_usesUniquePhoneAndEmail() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        Set<String> phoneValues = Arrays.stream(samplePersons)
                .map(person -> person.getPhone().value)
                .collect(Collectors.toSet());
        Set<String> normalizedEmailValues = Arrays.stream(samplePersons)
                .map(person -> person.getEmail().value.toLowerCase())
                .collect(Collectors.toSet());

        assertEquals(samplePersons.length, phoneValues.size());
        assertEquals(samplePersons.length, normalizedEmailValues.size());
    }

    @Test
    public void getSampleAddressBook_containsAllSamplePersons() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();

        assertEquals(Arrays.asList(SampleDataUtil.getSamplePersons()), sampleAddressBook.getPersonList());
    }
}
