package nl.tudelft.lifetiles.graph.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import nl.tudelft.lifetiles.core.controller.Controller;
import nl.tudelft.lifetiles.graph.model.DefaultGraphParser;
import nl.tudelft.lifetiles.graph.model.FactoryProducer;
import nl.tudelft.lifetiles.graph.model.Graph;
import nl.tudelft.lifetiles.graph.model.GraphFactory;
import nl.tudelft.lifetiles.graph.model.GraphParser;
import nl.tudelft.lifetiles.graph.model.sequence.SequenceSegment;
import nl.tudelft.lifetiles.graph.view.Tile;
import nl.tudelft.lifetiles.graph.view.TileView;
import nl.tudelft.lifetiles.sequence.controller.SequenceController;

/**
 * The controller of the graph view.
 *
 * @author Joren Hammudoglu
 *
 */
public class GraphController extends Controller {

    /**
     * The wrapper element.
     */
    @FXML
    private ScrollPane wrapper;

    /**
     * The currently loaded graph.
     */
    private Graph<SequenceSegment> graph;

    @Override
    public final void initialize(final URL location,
            final ResourceBundle resources) {
        register(Controller.GRAPH);
    }

    /**
     * @return the currently loaded graph.
     */
    public final Graph<SequenceSegment> getGraph() {
        if (!isLoaded()) {
            throw new UnsupportedOperationException("Graph not loaded.");
        }
        return graph;
    }

    /**
     * Load a new graph from the specified file.
     *
     * @param vertexfile
     *            The file to get vertices for.
     * @param edgefile
     *            The file to get edges for.
     * @throws IOException
     *             When an IO error occurs while reading one of the files.
     */
    public final void loadGraph(final File vertexfile, final File edgefile)
            throws IOException {
        // create the graph
        FactoryProducer<SequenceSegment> fp = new FactoryProducer<>();
        GraphFactory<SequenceSegment> gf = fp.getFactory("JGraphT");
        GraphParser gp = new DefaultGraphParser();
        graph = gp.parseGraph(vertexfile, edgefile, gf);

        // obtain the sequences
        SequenceController sequenceController = (SequenceController) getController(Controller.SEQUENCE);
        sequenceController.setSequences(gp.getSequences());
    }

    /**
     * Unload the graph and sequences.
     */
    public final void unloadGraph() {
        graph = null;
    }

    /**
     * Check if the graph is loaded.
     *
     * @return true if the graph is loaded
     */
    public final boolean isLoaded() {
        return graph != null;
    }

    @Override
    public final void repaint() {
        if (isLoaded()) {
            System.out.println("repainting graph...");
            Tile model = new Tile(graph);
            TileView view = new TileView();
            TileController tc = new TileController(view, model);

            Group root = tc.drawGraph();
            wrapper.setContent(root);
        }
    }

}
