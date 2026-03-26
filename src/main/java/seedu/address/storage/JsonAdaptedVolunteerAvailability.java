package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.VolunteerAvailability;

/**
 * Jackson-friendly version of {@link VolunteerAvailability}.
 */
class JsonAdaptedVolunteerAvailability {

    private final String value;

    @JsonCreator
    public JsonAdaptedVolunteerAvailability(@JsonProperty("value") String value) {
        this.value = value;
    }

    public JsonAdaptedVolunteerAvailability(VolunteerAvailability source) {
        this.value = source.dayOfWeek + "," + source.startTime + "," + source.endTime;
    }

    public VolunteerAvailability toModelType() throws IllegalValueException {
        if (!VolunteerAvailability.isValidAvailability(value)) {
            throw new IllegalValueException(VolunteerAvailability.MESSAGE_CONSTRAINTS);
        }
        return VolunteerAvailability.fromString(value);
    }
}
