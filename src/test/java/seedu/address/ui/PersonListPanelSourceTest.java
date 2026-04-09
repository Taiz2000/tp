package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

public class PersonListPanelSourceTest {

    private static final Path PERSON_LIST_PANEL_PATH =
            Path.of("src", "main", "java", "seedu", "address", "ui", "PersonListPanel.java");

    @Test
    public void personListPanel_setsPlaceholderForEmptyList() throws IOException {
        String source = Files.readString(PERSON_LIST_PANEL_PATH, StandardCharsets.UTF_8);

        assertTrue(source.contains("personListView.setPlaceholder(new Label(\"No contacts to display.\"));"));
    }
}
