package nl.tudelft.lifetiles.graph.model.jgrapht;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nl.tudelft.lifetiles.core.util.Logging;
import nl.tudelft.lifetiles.graph.model.Edge;
import nl.tudelft.lifetiles.graph.model.Graph;
import nl.tudelft.lifetiles.graph.model.GraphFactory;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedSubgraph;

/**
 * @author Rutger van den Berg
 *
 * @param <V>
 *            The type of Vertex to use.
 */
public class JGraphTGraphFactory<V extends Comparable<V>> implements
        GraphFactory<V> {
    /**
     * The edgefactory associated with this graph factory.
     */
    private final JGraphTEdgeFactory<V> edgeFact;

    /**
     * Create a new graph factory.
     */
    public JGraphTGraphFactory() {
        edgeFact = new JGraphTEdgeFactory<>();
    }

    /**
     * @return a new empty Graph.
     */
    @Override
    public Graph<V> getGraph() {
        return new JGraphTGraphAdapter<V>(edgeFact);
    }

    /**
     * @param base
     *            the graph to create a subgraph from.
     * @param vertexSubSet
     *            the vertices to include in the subgraph
     * @return a subgraph based on the base graph
     * @throws NotAJGraphTAdapterException
     *             if the base graph is not a JGraphT library
     */
    @Override
    public Graph<V> getSubGraph(final Graph<V> base, final Set<V> vertexSubSet)
            throws NotAJGraphTAdapterException {

        if (base instanceof JGraphTGraphAdapter) {
            JGraphTGraphAdapter<V> baseGraph = (JGraphTGraphAdapter<V>) base;

            return new JGraphTGraphAdapter<V>(
                    new DirectedSubgraph<V, DefaultEdge>(
                            baseGraph.getInternalGraph(), vertexSubSet, null),
                    edgeFact, baseGraph.getVertexIdentifiers());

        } else {
            throw new NotAJGraphTAdapterException();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Graph<V> deepcopy(final Graph<V> graph) {
        Graph<V> copygraph = new JGraphTGraphAdapter<V>(edgeFact);

        Map<Object, Object> convertVertices = new HashMap<Object, Object>();

        for (V vertex : graph.getAllVertices()) {
            try {
                Constructor<?> method = vertex.getClass().getConstructor(
                        vertex.getClass());

                Object copy = method.newInstance(vertex);
                // can't not be a V, so no need to explicitly check.
                @SuppressWarnings("unchecked")
                V newVertex = (V) copy;
                copygraph.addVertex(newVertex);
                convertVertices.put(vertex, copy);
            } catch (NoSuchMethodException | SecurityException
                    | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | InstantiationException exception) {

                Logging.exception(exception);
            }
        }

        for (Edge<V> edge : graph.getAllEdges()) {
            Object from = convertVertices.get(graph.getSource(edge));
            Object destination = convertVertices
                    .get(graph.getDestination(edge));
            @SuppressWarnings("unchecked")
            V fromVertex = (V) from;
            @SuppressWarnings("unchecked")
            V destinationVertex = (V) destination;
            copygraph.addEdge(fromVertex, destinationVertex);
        }

        return copygraph;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Graph<V> copy(final Graph<V> graph) {
        Graph<V> copyGraph = new JGraphTGraphAdapter<V>(edgeFact);
        for (V vertex : graph.getAllVertices()) {
            copyGraph.addVertex(vertex);
        }
        for (Edge<V> edge : graph.getAllEdges()) {
            copyGraph
                    .addEdge(graph.getSource(edge), graph.getDestination(edge));
        }
        return copyGraph;
    }

}
