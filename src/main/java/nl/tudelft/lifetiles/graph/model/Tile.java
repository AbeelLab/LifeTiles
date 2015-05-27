package nl.tudelft.lifetiles.graph.model;

import nl.tudelft.lifetiles.graph.traverser.EmptySegmentTraverser;
import nl.tudelft.lifetiles.graph.traverser.MutationIndicationTraverser;
import nl.tudelft.lifetiles.graph.traverser.ReferencePositionTraverser;
import nl.tudelft.lifetiles.graph.traverser.UnifiedPositionTraverser;
import nl.tudelft.lifetiles.sequence.model.Sequence;
import nl.tudelft.lifetiles.sequence.model.SequenceSegment;

/**
 * The Tile holds the graph and will be transformed to this modelgraph so
 * that the graph can be drawn on the screen.
 *
 */
public class Tile {

    /**
     * The Current graph that this model is holding.
     */
    private Graph<SequenceSegment> graph;

    /**
     * create a new Tile.
     *
     * @param gr
     *            The initial graph
     */
    public Tile(final Graph<SequenceSegment> gr) {
        graph = gr;

        // TODO: Temporary line until sequence selection is implemented.
        Sequence reference = graph.getSources().iterator().next().getSources()
                .iterator().next();
        
        alignGraph();
        findMutations(reference);
    }

    /**
     * Align the graph.
     */
    private void alignGraph() {
        UnifiedPositionTraverser upt = new UnifiedPositionTraverser();
        EmptySegmentTraverser est = new EmptySegmentTraverser();
        graph = est.addEmptySegmentsGraph(upt.unifyGraph(graph));
    }

    /**
     * Find the mutations on the graph.
     *
     * @param reference
     *            Reference of the graph which is used to indicate mutations.
     */
    private void findMutations(final Sequence reference) {
        ReferencePositionTraverser rmt = new ReferencePositionTraverser(reference);
        MutationIndicationTraverser mt = new MutationIndicationTraverser(reference);
        graph = mt.indicateGraphMutations(rmt.referenceMapGraph(graph));
    }

    /**
     * Get the graph that this model is holding.
     *
     * @return graph
     */
    public final Graph<SequenceSegment> getGraph() {
        return graph;
    }

}