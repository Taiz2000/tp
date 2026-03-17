package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.Test;

import seedu.address.MainApp;

/**
 * Reuse testing code from Codex.
 */
public class UiResourceSmokeTest {

    private static final String[] CORE_FXML_FILES = {
        "/view/MainWindow.fxml",
        "/view/CommandBox.fxml",
        "/view/ResultDisplay.fxml",
        "/view/PersonListPanel.fxml",
        "/view/PersonListCard.fxml",
        "/view/StatusBarFooter.fxml",
        "/view/HelpWindow.fxml"
    };

    private static final String[] CORE_STYLESHEETS = {
        "/view/DarkTheme.css",
        "/view/Extensions.css",
        "/view/HelpWindow.css"
    };

    private static final String[] CORE_IMAGES = {
        "/images/address_book_32.png",
        "/images/help_icon.png",
        "/images/info_icon.png"
    };

    @Test
    public void coreFxmlFiles_existAndAreWellFormed() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        for (String fxmlPath : CORE_FXML_FILES) {
            URL resourceUrl = MainApp.class.getResource(fxmlPath);
            assertNotNull(resourceUrl, fxmlPath + " should exist");

            try (InputStream inputStream = resourceUrl.openStream()) {
                factory.newDocumentBuilder().parse(inputStream);
            }
        }
    }

    @Test
    public void coreStylesheets_exist() {
        for (String stylesheetPath : CORE_STYLESHEETS) {
            assertNotNull(MainApp.class.getResource(stylesheetPath), stylesheetPath + " should exist");
        }
    }

    @Test
    public void coreImages_exist() {
        for (String imagePath : CORE_IMAGES) {
            assertNotNull(MainApp.class.getResource(imagePath), imagePath + " should exist");
        }
    }
}
