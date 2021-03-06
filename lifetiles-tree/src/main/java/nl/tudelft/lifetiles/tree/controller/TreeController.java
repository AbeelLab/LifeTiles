package nl.tudelft.lifetiles.tree.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import nl.tudelft.lifetiles.core.controller.AbstractController;
import nl.tudelft.lifetiles.core.controller.MenuController;
import nl.tudelft.lifetiles.core.util.Logging;
import nl.tudelft.lifetiles.core.util.Message;
import nl.tudelft.lifetiles.core.util.Timer;
import nl.tudelft.lifetiles.sequence.model.Sequence;
import nl.tudelft.lifetiles.tree.model.PhylogeneticTreeItem;
import nl.tudelft.lifetiles.tree.model.PhylogeneticTreeParser;
import nl.tudelft.lifetiles.tree.view.SunburstView;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The controller of the tree view.
 *
 * @author Albert Smit
 *
 */
public class TreeController extends AbstractController {

    /**
     * The diagram.
     */
    @FXML
    private SunburstView view;
    /**
     * the parent node.
     */
    @FXML
    private BorderPane wrapper;

    /**
     * The tree model.
     */
    private PhylogeneticTreeItem tree;

    /**
     * The visible tree model.
     */
    private PhylogeneticTreeItem visibleTree;

    /**
     * The model of sequences.
     */

    private Map<String, Sequence> sequences;

    /**
     * {@inheritDoc}
     */
    @Override
    // checkstyle bug causes false positives, so we'll suppress them(for now).
    @SuppressWarnings("checkstyle:genericwhitespace")
    public void initialize(final URL location, final ResourceBundle resources) {
        // load the tree when the files are opened
        listen(Message.OPENED, (controller, subject, args) -> {
            assert controller instanceof MenuController;
            if (!"tree".equals(subject)) {
                return;
            }
            assert args.length == 1;
            assert args[0] instanceof File;
            try {
                loadTree((File) args[0]);
            } catch (FileNotFoundException e) {
                Logging.exception(e);
            }
        });

        listen(Message.LOADED,
                (controller, subject, args) -> {
                    if (!"sequences".equals(subject)) {
                        return;
                    }
                    assert args[0] instanceof Map<?, ?>;
                    @SuppressWarnings("unchecked")
                    Map<String, Sequence> newSequences = (Map<String, Sequence>) args[0];
                    sequences = newSequences;
                    repaint();
                });

        listen(Message.FILTERED, (controller, subject, args) -> {
            // check the message is correct
                assert args.length == 1;
                assert args[0] instanceof Set<?>;
                if (!(controller instanceof TreeController)) {
                    // create the new tree
                @SuppressWarnings("unchecked")
                Set<Sequence> newSequences = (Set<Sequence>) args[0];
                setVisible(newSequences);
            }
        });

        // inform the sunburst of this controller so filters can be shouted
        view.setController(this);
        view.setBounds(wrapper.layoutBoundsProperty().get());
        wrapper.layoutBoundsProperty().addListener(
                (observableBounds, oldValue, newValue) -> view
                        .setBounds(newValue));
    }

    /**
     * Loads the tree located in the file.
     *
     * @param file
     *            The .nwk file
     * @throws FileNotFoundException
     *             when the file is not found
     */
    // simple files, so default encoding is fine. Better to keep things
    // flexible with respect to what files can be parsed.
    @SuppressFBWarnings("DM_DEFAULT_ENCODING")
    private void loadTree(final File file) throws FileNotFoundException {
        // convert the file to a single string
        String fileString = null;
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("\\Z");

        fileString = scanner.next();
        scanner.close();

        // parse the string into a tree
        tree = PhylogeneticTreeParser.parse(fileString);
        linkSequence(sequences, tree);
        tree.populateChildSequences();
        visibleTree = tree;

        repaint();

        shout(Message.LOADED, "tree", tree);
    }

    /**
     * Repaints the view.
     */
    private void repaint() {
        if (visibleTree != null) {
            view.setRoot(visibleTree);
        }
    }

    /**
     * Add the sequences to a tree, a sequence will be added to a node if the
     * node's name matches the sequence's id.
     *
     * @param sequences
     *            a map containing the sequences and their identifiers
     * @param node
     *            the root of a phylogenetic tree
     */
    private void linkSequence(final Map<String, Sequence> sequences,
            final PhylogeneticTreeItem node) {
        String ident = node.getName();
        Sequence sequence = sequences.get(ident);
        node.setSequence(sequence);

        for (PhylogeneticTreeItem child : node.getChildren()) {
            linkSequence(sequences, child);
        }
    }

    /**
     * Sets the sequences in the set to visible, and creates a tree that only
     * contains the visible sequences.
     *
     * @param visible
     *            A set containing all visible sequences
     */
    private void setVisible(final Set<Sequence> visible) {
        Timer timer = Timer.getAndStart();
        visibleTree = tree.subTree(visible);

        linkSequence(sequences, visibleTree);
        visibleTree.populateChildSequences();

        timer.stopAndLog("creating subtree");
        repaint();
    }

    /**
     * Informs other controllers the filter changed.
     *
     * @param visible
     *            the set that needs to be visible
     */
    public void shoutVisible(final Set<Sequence> visible) {
        shout(Message.FILTERED, "", visible);
    }
}
