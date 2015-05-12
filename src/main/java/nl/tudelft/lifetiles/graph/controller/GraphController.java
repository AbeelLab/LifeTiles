package nl.tudelft.lifetiles.graph.controller;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

import nl.tudelft.lifetiles.graph.controller.traverser.AbsoluteProjectionTraverser;
import nl.tudelft.lifetiles.graph.controller.traverser.AlignmentTraverser;
import nl.tudelft.lifetiles.graph.controller.traverser.MutationTraverser;
import nl.tudelft.lifetiles.graph.models.DefaultGraphParser;
import nl.tudelft.lifetiles.graph.models.FactoryProducer;
import nl.tudelft.lifetiles.graph.models.Graph;
import nl.tudelft.lifetiles.graph.models.GraphFactory;
import nl.tudelft.lifetiles.graph.models.Tile;
import nl.tudelft.lifetiles.graph.models.sequence.DefaultSequence;
import nl.tudelft.lifetiles.graph.models.sequence.SegmentString;
import nl.tudelft.lifetiles.graph.models.sequence.Sequence;
import nl.tudelft.lifetiles.graph.models.sequence.SequenceSegment;
import nl.tudelft.lifetiles.graph.view.TileView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;

/**
 * The controller of the graph view.
 *
 * @author Joren Hammudoglu
 *
 */
public class GraphController implements Initializable {

    /**
     * The wrapper element.
     */
    @FXML
    private ScrollPane wrapper;

    /**
     * Initializes the Graph View.
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        Tile model = new Tile(loadGraphModel());
        TileView view = new TileView();
        TileController controller = new TileController(view, model);

        Group root = controller.drawGraph();
        wrapper.setContent(root);
    }

    /**
     * Loads a graph by filename.
     * 
     * @return parsed graph by filename.
     */
    private Graph<SequenceSegment> loadGraphModel() {
        FactoryProducer<SequenceSegment> fp = new FactoryProducer<SequenceSegment>();
        GraphFactory<SequenceSegment> gf = fp.getFactory("JGraphT");
        DefaultGraphParser parser = new DefaultGraphParser();
        Graph<SequenceSegment> gr = parser.parseFile(
                "data/simple_graph/simple_graph", gf);
        return gr;
    }

}
