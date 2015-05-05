package nl.tudelft.lifetiles.graph;

import static org.junit.Assert.assertEquals;
import nl.tudelft.lifetiles.graph.sequence.SegmentEmpty;
import nl.tudelft.lifetiles.graph.sequence.SegmentString;
import nl.tudelft.lifetiles.graph.sequence.SequenceSegment;

import org.junit.Before;
import org.junit.Test;

public class SequenceSegmentTest {

    SequenceSegment v1, v2, v3;

    @Before
    public void setUp() throws Exception {
        v1 = new SequenceSegment(null, 1, 10, new SegmentString("AAAAAAAAAA"));
        v2 = new SequenceSegment(null, 11, 20, new SegmentEmpty(10));
        v3 = new SequenceSegment(null, 21, 30, new SegmentString("AAAAAAAAAA"));
    }
    
    @Test
    public void testDistanceToAdjacent() {
    	assertEquals(0, v1.distanceTo(v2));
    }
    
    @Test
    public void testDistanceToPositive() {
    	assertEquals(10, v1.distanceTo(v3));
    }
    
    @Test
    public void testDistanceToNegative() {
    	assertEquals(-30, v3.distanceTo(v1));
    }

    @Test
    public void testLengthEmpty() {
    	assertEquals(10, v2.getContent().length());
    }
    @Test
    public void testLengthString() {
    	assertEquals(10, v1.getContent().length());
    }
}