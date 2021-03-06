package nl.tudelft.lifetiles.core.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import nl.tudelft.lifetiles.core.util.Message;

/**
 * The controller of the main view.
 *
 * @author Joren Hammudoglu
 *
 */
public class MainController extends AbstractController {

    /**
     * The main grid element.
     */
    @FXML
    private SplitPane mainSplitPane;

    /**
     * The content to be displayed when the data is not yet loaded.
     */
    @FXML
    private HBox splashPane;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location,
            final ResourceBundle resources) {
        mainSplitPane.setVisible(false);

        repaint(true);

        listen(Message.OPENED, (controller, subject, args) -> {
            repaint(false);
        });
    }

    /**
     * Repaint the main view, showing or hiding the splash screen.
     *
     * @param splash
     *            show the splash
     */
    private void repaint(final boolean splash) {
        mainSplitPane.setVisible(!splash);
        splashPane.setVisible(splash);
    }

}
