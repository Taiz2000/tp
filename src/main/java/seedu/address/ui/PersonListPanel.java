package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private Label personListStatus;

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given status text and {@code ObservableList}.
     */
    public PersonListPanel(String status, ObservableList<Person> personList) {
        super(FXML);
        requireNonNull(status);
        requireNonNull(personList);

        setPersonListStatus(status);

        personListView.setPlaceholder(new Label("No contacts to display."));
        setPersonList(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
    }

    public void setPersonListStatus(String status) {
        personListStatus.setText(status);
    }

    /**
     * Rebinds this panel to display a different filtered list.
     */
    public void setPersonList(ObservableList<Person> personList) {
        personListView.setItems(personList);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }
}
