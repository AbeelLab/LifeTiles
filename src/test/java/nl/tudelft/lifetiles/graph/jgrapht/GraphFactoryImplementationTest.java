package nl.tudelft.lifetiles.graph.jgrapht;

import nl.tudelft.lifetiles.graph.models.FactoryProducer;
import nl.tudelft.lifetiles.graph.models.Graph;
import nl.tudelft.lifetiles.graph.models.GraphFactory;
import nl.tudelft.lifetiles.graph.models.JGraphTGraph;
import nl.tudelft.lifetiles.graph.models.sequence.SequenceSegment;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GraphFactoryImplementationTest {
    GraphFactory<SequenceSegment> gf;
    static FactoryProducer<SequenceSegment> fp;
    SequenceSegment v1, v2;

    @BeforeClass
    public static void runOnce() {
        fp = new FactoryProducer<SequenceSegment>();
    }

    @Before
    public void setUp() throws Exception {
        gf = fp.getFactory("JGraphT");
        v1 = new SequenceSegment(null, 0, 0, null);
        v2 = new SequenceSegment(null, 0, 0, null);
    }

    @Test
    public void testGetGraph() {
        Graph<SequenceSegment> g = gf.getGraph();
        assert (g instanceof JGraphTGraph);
    }
}
