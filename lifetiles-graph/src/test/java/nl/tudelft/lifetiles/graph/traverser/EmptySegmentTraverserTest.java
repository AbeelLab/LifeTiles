package nl.tudelft.lifetiles.graph.traverser;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import nl.tudelft.lifetiles.core.util.Logging;
import nl.tudelft.lifetiles.graph.model.FactoryProducer;
import nl.tudelft.lifetiles.graph.model.Graph;
import nl.tudelft.lifetiles.graph.model.GraphFactory;
import nl.tudelft.lifetiles.sequence.model.DefaultSequence;
import nl.tudelft.lifetiles.sequence.model.SegmentEmpty;
import nl.tudelft.lifetiles.sequence.model.SegmentString;
import nl.tudelft.lifetiles.sequence.model.Sequence;
import nl.tudelft.lifetiles.sequence.model.SequenceSegment;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmptySegmentTraverserTest {
    GraphFactory<SequenceSegment> gf;
    static EmptySegmentTraverser at;
    static UnifiedPositionTraverser pt;
    static Set<Sequence> s1, s2, s3;
    SequenceSegment v1, v2, v3;
    Graph<SequenceSegment> gr;

    @BeforeClass
    public static void runOnce() {
        Logging.setLevel(Level.SEVERE);
        Sequence ss1 = new DefaultSequence("reference");
        Sequence ss2 = new DefaultSequence("mutation");

        s1 = new HashSet<Sequence>();
        s1.add(ss1);
        s1.add(ss2);

        s2 = new HashSet<Sequence>();
        s2.add(ss1);

        s3 = new HashSet<Sequence>();
        s3.add(ss2);
    }

    @Before
    public void setUp() throws Exception {
        gf = FactoryProducer.getFactory("JGraphT");
        v1 = new SequenceSegment(s1, 1, 11, new SegmentString("AAAAAAAAAA"));
        v2 = new SequenceSegment(s3, 11, 21, new SegmentEmpty(10));
        v3 = new SequenceSegment(s1, 21, 31, new SegmentString("AAAAAAAAAA"));
        gr = gf.getGraph();
        gr.addVertex(v1);
        gr.addVertex(v3);
    }

    @Test
    public void testTraverseForkGraph() {
        SequenceSegment v4 = new SequenceSegment(s2, 1, 11, new SegmentString(
                "AAAAAAAAAA"));
        gr.addVertex(v4);
        gr.addEdge(v1, v3);
        gr.addEdge(v4, v3);
        UnifiedPositionTraverser.unifyGraph(gr);
        EmptySegmentTraverser.addEmptySegmentsGraph(gr);
        assertEquals(3, gr.getAllVertices().size());
    }

    @Test
    public void testTraverseBranchGraph() {
        SequenceSegment v4 = new SequenceSegment(s2, 21, 31, new SegmentString(
                "AAAAAAAAAA"));
        gr.addVertex(v4);
        gr.addEdge(v1, v3);
        gr.addEdge(v1, v4);
        UnifiedPositionTraverser.unifyGraph(gr);
        EmptySegmentTraverser.addEmptySegmentsGraph(gr);
        assertEquals(3, gr.getAllVertices().size());
    }

    @Test
    public void testTraverseGapGraph() {
        gr.addEdge(v1, v3);
        UnifiedPositionTraverser.unifyGraph(gr);
        EmptySegmentTraverser.addEmptySegmentsGraph(gr);
        assertEquals(2, gr.getAllVertices().size());
    }

    @Test
    public void testTraverseGapCoordinatesGraph() {
        gr.addEdge(v1, v3);
        UnifiedPositionTraverser.unifyGraph(gr);
        EmptySegmentTraverser.addEmptySegmentsGraph(gr);
        assertEquals(11, gr.getSource(gr.getIncoming(v3).iterator().next())
                .getEnd());
        assertEquals(1, gr.getSource(gr.getIncoming(v3).iterator().next())
                .getStart());
    }

    @Test
    public void testTraverseBridgeGraph() {
        gr.addVertex(v2);
        gr.addEdge(v1, v2);
        gr.addEdge(v2, v3);
        UnifiedPositionTraverser.unifyGraph(gr);
        EmptySegmentTraverser.addEmptySegmentsGraph(gr);
        assertEquals(3, gr.getAllVertices().size());
    }

    @Test
    public void testTraverseInsertionGraph() {
        SequenceSegment v4 = new SequenceSegment(s2, 11, 21, new SegmentString(
                "AAAAAAAAAA"));
        gr.addVertex(v4);
        gr.addEdge(v1, v3);
        gr.addEdge(v1, v4);
        gr.addEdge(v4, v3);
        UnifiedPositionTraverser.unifyGraph(gr);
        EmptySegmentTraverser.addEmptySegmentsGraph(gr);
        assertEquals(4, gr.getAllVertices().size());
    }

}
