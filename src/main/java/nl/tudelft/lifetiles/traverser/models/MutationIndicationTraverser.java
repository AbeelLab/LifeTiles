package nl.tudelft.lifetiles.traverser.models;

import nl.tudelft.lifetiles.graph.models.Graph;
import nl.tudelft.lifetiles.graph.models.sequence.SegmentEmpty;
import nl.tudelft.lifetiles.graph.models.sequence.Sequence;
import nl.tudelft.lifetiles.graph.models.sequence.SequenceSegment;
import nl.tudelft.lifetiles.graph.view.Mutation;

/**
 * Indicates mutations by traversing over all the vertices.
 *
 * @author Jos
 *
 */
public class MutationIndicationTraverser {

    /**
     * Reference which is compared to determine the mutation types.
     */
    private Sequence referenceVar;

    /**
     * Constructs a MutationIndicationTraverser.
     *
     * @param reference
     *            Reference which is compared to determine the mutation types.
     */
    public MutationIndicationTraverser(final Sequence reference) {
        referenceVar = reference;
    }

    /**
     * Copy of the graph which is being traversed.
     */
    private Graph<SequenceSegment> graphVar;

    /**
     * Traverses the copy of the graph and indicates the mutation types.
     *
     * @param graph
     *            The graph to copy and traverse.
     * @return the traversed copy of the graph.
     */
    public final Graph<SequenceSegment> traverseGraph(
            final Graph<SequenceSegment> graph) {
        graphVar = graph.copy();
        traverseGraph();
        return graphVar;
    }

    /**
     * Traverse the copy of the graph and indicates the mutation types.
     *
     * @return traversed copy of the graph.
     */
    public final Graph<SequenceSegment> traverseGraph() {
        for (SequenceSegment vertex : graphVar.getAllVertices()) {
            traverseVertex(vertex);
        }
        return graphVar;
    }

    /**
     * Traverse a vertex in the copy of the graph and determines the mutation
     * type of the mutation, if it has one.
     *
     * @param vertex
     *            Vertex in the copy of the graph to be traversed.
     */
    private void traverseVertex(final SequenceSegment vertex) {
        if (!vertex.getSources().contains(referenceVar)) {
            if (vertex.getContent() instanceof SegmentEmpty) {
                vertex.setMutation(Mutation.DELETION);
            } else if (vertex.getReferenceStart() > vertex.getReferenceEnd()) {
                vertex.setMutation(Mutation.INSERTION);
            } else {
                vertex.setMutation(Mutation.POLYMORPHISM);
            }
        }
    }

}
