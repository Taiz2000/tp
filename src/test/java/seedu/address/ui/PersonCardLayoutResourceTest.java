package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import seedu.address.MainApp;

/**
 * Reuse testing code from Codex.
 */
public class PersonCardLayoutResourceTest {

    private static final String PERSON_CARD_FXML = "/view/PersonListCard.fxml";
    private static final String DARK_THEME_CSS = "/view/DarkTheme.css";

    @Test
    public void personListCardFxml_enablesWrappingForLongFields() throws IOException {
        String fxmlContent = readResourceContent(PERSON_CARD_FXML);

        assertTrue(fxmlContent.contains(
            "fx:id=\"name\" text=\"\\$first\" styleClass=\"cell_big_label\" wrapText=\"true\""
        ));
        assertTrue(fxmlContent.contains(
            "fx:id=\"phone\" styleClass=\"cell_small_label\" text=\"\\$phone\" wrapText=\"true\""
        ));
        assertTrue(fxmlContent.contains(
            "fx:id=\"address\" styleClass=\"cell_small_label\" text=\"\\$address\" wrapText=\"true\""
        ));
        assertTrue(fxmlContent.contains(
            "fx:id=\"email\" styleClass=\"cell_small_label\" text=\"\\$email\" wrapText=\"true\""
        ));
        assertTrue(fxmlContent.contains(
            "fx:id=\"role\" styleClass=\"cell_small_label\" text=\"\\$role\" wrapText=\"true\""
        ));
        assertTrue(fxmlContent.contains(
            "fx:id=\"notes\" styleClass=\"cell_small_label\" text=\"\\$notes\" wrapText=\"true\""
        ));
        assertTrue(fxmlContent.contains(
            "fx:id=\"tags\" prefWrapLength=\"240\""
        ));
    }

    @Test
    public void darkThemeCss_setsCardAndTagSpacingForReadability() throws IOException {
        String cssContent = readResourceContent(DARK_THEME_CSS);

        assertTrue(cssContent.contains("#cardPane {"));
        assertTrue(cssContent.contains("-fx-padding: 2 0 2 0;"));
        assertTrue(cssContent.contains("#tags .label {"));
        assertTrue(cssContent.contains("-fx-padding: 2 6 2 6;"));
    }

    private static String readResourceContent(String resourcePath) throws IOException {
        URL resourceUrl = MainApp.class.getResource(resourcePath);
        assertNotNull(resourceUrl, resourcePath + " should exist");
        return new String(resourceUrl.openStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
