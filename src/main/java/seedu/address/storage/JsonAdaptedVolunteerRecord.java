package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.VolunteerRecord;

/**
 * Jackson-friendly version of {@link VolunteerRecord}.
 */
class JsonAdaptedVolunteerRecord {

    private final String value;

    @JsonCreator
    public JsonAdaptedVolunteerRecord(@JsonProperty("value") String value) {
        this.value = value;
    }

    public JsonAdaptedVolunteerRecord(VolunteerRecord source) {
        this.value = source.startDateTime + "," + source.endDateTime;
    }

    public VolunteerRecord toModelType() throws IllegalValueException {
        if (!VolunteerRecord.isValidRecord(value)) {
            throw new IllegalValueException(VolunteerRecord.MESSAGE_CONSTRAINTS);
        }
        return VolunteerRecord.fromString(value);
    }
}
