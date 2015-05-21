package nl.tudelft.lifetiles.traverser.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import nl.tudelft.lifetiles.graph.models.Edge;
import nl.tudelft.lifetiles.graph.models.Graph;
import nl.tudelft.lifetiles.graph.models.sequence.SegmentEmpty;
import nl.tudelft.lifetiles.graph.models.sequence.Sequence;
import nl.tudelft.lifetiles.graph.models.sequence.SequenceSegment;

/**
 * Places empty vertices in the graph where there are unified gaps.
 *
 * @author Jos
 *
 */
public class EmptySegmentTraverser implements Traverser {
    /**
     * Traverser's graph.
     */
    private Graph<SequenceSegment> graphVar;

    /**
     * Map which maps edges to sequence segments, used to make
     * modifications on during traversal. Inserted into graph at end of
     * traversal to avoid concurrent modification-like exceptions.
     */
    private HashMap<Edge<SequenceSegment>, SequenceSegment> emptySegments;

    /**
     * Traverses the graph. Adds empty vertices to the graph which are being
     * used to indicate mutations on.
     *
     * @param graph
     *            the graph to traverse.
     * @return the traversed of the graph.
     */
    public final Graph<SequenceSegment> traverseGraph(
            final Graph<SequenceSegment> graph) {
        graphVar = graph;
        addEmptySegmentsGraph();
        return this.graphVar;
    }

    /**
     * Traverses the graph. Adds empty vertices to the graph which are being
     * used to indicate mutations on.
     */
    private void addEmptySegmentsGraph() {
        emptySegments = new HashMap<Edge<SequenceSegment>, SequenceSegment>();
        for (SequenceSegment vertex : graphVar.getAllVertices()) {
            addEmptySegmentsVertex(vertex);
        }
        for (Entry<Edge<SequenceSegment>, SequenceSegment> entry : emptySegments
                .entrySet()) {
            Edge<SequenceSegment> edge = entry.getKey();
            SequenceSegment vertex = entry.getValue();
            graphVar.splitEdge(edge, vertex);
        }
    }

    /**
     * Traverses a vertex in the graph. Adds empty vertices to the graph which
     * are being used to indicate mutations on.
     *
     * @param vertex
     *            the vertex to traverse.
     */
    private void addEmptySegmentsVertex(final SequenceSegment vertex) {
        Set<Sequence> buffer = new HashSet<Sequence>(vertex.getSources());
        for (Edge<SequenceSegment> edge : graphVar.getOutgoing(vertex)) {
            SequenceSegment destination = graphVar.getDestination(edge);
            Set<Sequence> sources = new HashSet<Sequence>(
                    destination.getSources());
            sources.retainAll(buffer);
            if (vertex.distanceTo(destination) > 0) {
                emptySegments.put(edge,
                        bridgeSequence(vertex, destination, sources));
            }
            buffer.removeAll(sources);
        }
    }

    /**
     * Creates a bridge sequence segment between a source and destination
     * segment.
     *
     * @param source
     *            The vertex that is the source segment.
     * @param destination
     *            The vertex that is the destination segment.
     * @param sources
     *            The set of sources in the to be constructed segment.
     * @return sequence segment between source and destination segment
     */
    private SequenceSegment bridgeSequence(final SequenceSegment source,
            final SequenceSegment destination, final Set<Sequence> sources) {
        SequenceSegment vertex = new SequenceSegment(sources,
                source.getUnifiedEnd(), destination.getUnifiedStart(),
                new SegmentEmpty(source.distanceTo(destination) + 1));
        vertex.setUnifiedStart(vertex.getStart());
        vertex.setUnifiedEnd(vertex.getEnd());
        return vertex;
    }
}