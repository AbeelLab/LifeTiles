package nl.tudelft.lifetiles.controller;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import nl.tudelft.lifetiles.graph.FactoryProducer;
import nl.tudelft.lifetiles.graph.Graph;
import nl.tudelft.lifetiles.graph.GraphFactory;
import nl.tudelft.lifetiles.graph.sequence.SegmentString;
import nl.tudelft.lifetiles.graph.sequence.Sequence;
import nl.tudelft.lifetiles.graph.sequence.SequenceImplementation;
import nl.tudelft.lifetiles.graph.sequence.SequenceSegment;
import nl.tudelft.lifetiles.graph.traverser.AbsoluteProjectionTraverser;
import nl.tudelft.lifetiles.graph.traverser.AlignmentTraverser;
import nl.tudelft.lifetiles.graph.traverser.MutationTraverser;
import nl.tudelft.lifetiles.tilegraph.TileController;
import nl.tudelft.lifetiles.tilegraph.TileModel;
import nl.tudelft.lifetiles.tilegraph.TileView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * The controller of the graph view.
 * 
 * @author Joren Hammudoglu
 *
 */
@SuppressWarnings("restriction")
public class GraphController implements Initializable {

    /**
     * The wrapper element.
     */
    @FXML
    private ScrollPane wrapper;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        TileModel model = new TileModel(loadGraphModel());
        TileView view = new TileView();
        TileController controller = new TileController(view, model);
        view.addController(controller);

        Group root = controller.drawGraph();
        wrapper.setContent(root);
    }

    private Graph<SequenceSegment> loadGraphModel() {
        HashSet<Sequence> s1, s2, s3;
        SequenceSegment v1, v2, v3, v4, v5, v6;
        FactoryProducer<SequenceSegment> fp = new FactoryProducer<SequenceSegment>();
        GraphFactory<SequenceSegment> gf = fp.getFactory("JGraphT");
        Graph<SequenceSegment> gr = gf.getGraph();

        Sequence ss1 = new SequenceImplementation("reference");
        Sequence ss2 = new SequenceImplementation("mutation_1");
        Sequence ss3 = new SequenceImplementation("mutation_2");
        Sequence ss4 = new SequenceImplementation("mutation_3");

        s1 = new HashSet<Sequence>();
        s1.add(ss1);
        s1.add(ss2);
        s1.add(ss3);
        s1.add(ss4);

        s2 = new HashSet<Sequence>();
        s2.add(ss1);
        s2.add(ss2);

        s3 = new HashSet<Sequence>();
        s3.add(ss3);
        s3.add(ss4);

        v1 = new SequenceSegment(s1, 1, 11, new SegmentString("ACTGGTTCGG"));
        v2 = new SequenceSegment(s2, 11, 21, new SegmentString("TCGGACTGGT"));
        v3 = new SequenceSegment(s3, 11, 16, new SegmentString("GGTTC"));
        v4 = new SequenceSegment(s1, 21, 31, new SegmentString("TGGGACTTCG"));
        v5 = new SequenceSegment(s3, 31, 41, new SegmentString("CTTCTGGGAG"));
        v6 = new SequenceSegment(s1, 41, 46, new SegmentString("GAGCG"));

        gr.addVertex(v1);
        gr.addVertex(v2);
        gr.addVertex(v3);
        gr.addVertex(v4);
        gr.addVertex(v5);
        gr.addVertex(v6);

        gr.addEdge(v1, v2);
        gr.addEdge(v1, v3);
        gr.addEdge(v2, v4);
        gr.addEdge(v3, v4);
        gr.addEdge(v4, v5);
        gr.addEdge(v5, v6);
        gr.addEdge(v4, v6);

        AlignmentTraverser at = new AlignmentTraverser();
        AbsoluteProjectionTraverser apt = new AbsoluteProjectionTraverser(ss1);
        MutationTraverser mt = new MutationTraverser(ss1);

        at.traverseGraph(gr);
        apt.traverseGraph(gr);
        mt.traverseGraph(gr);

        return gr;
    }
}
