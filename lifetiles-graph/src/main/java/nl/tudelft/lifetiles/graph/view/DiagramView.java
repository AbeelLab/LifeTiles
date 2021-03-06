package nl.tudelft.lifetiles.graph.view;

import java.util.Map;

import javafx.scene.Group;
import nl.tudelft.lifetiles.graph.model.StackedMutationContainer;

/**
 * Diagram view which contains a view of the stacked mutation diagram, which is
 * a stacked representation of the percentage of mutations in a bucket or a
 * merged bucket.
 *
 * @author Jos
 *
 */
public class DiagramView {

    /**
     * Computes the stack level to display and draws the diagram view to the
     * screen.
     *
     * @param container
     *            The stacked mutation model which contains the data to display.
     * @param zoomLevel
     *            The zoom level of the graph controller used to determine the
     *            stacked mutation container layer to display.
     * @param width
     *            The width of the diagram view based on the scale in the graph
     *            controller.
     * @return a group containing the stack views to draw.
     */
    public Group drawDiagram(final StackedMutationContainer container,
            final int zoomLevel, final double width) {
        Map<Integer, StackedMutationContainer> containers = container
                .mapLevelStackedMutation();
        int stackLevel = Math.max(1, Math.min(zoomLevel, container.getLevel()));
        StackedMutationContainer stack = containers.get(stackLevel);
        return drawStackContainer(stack, width);
    }

    /**
     * Displays the stacks in the diagram view.
     *
     * @param stack
     *            The stack layer to display.
     * @param width
     *            The width of the stacks.
     * @return group with diagram containing a set of stack views.
     */
    private Group drawStackContainer(final StackedMutationContainer stack,
            final double width) {
        Group root = new Group();
        int stacks = stack.getStack().size();
        double stackWidth = width / stacks;
        for (int index = 0; index < stacks; index++) {
            // The purpose of this loop is to create these, so suppress the
            // warning.
            @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
            StackView stackView = new StackView(stack.getStack().get(index),
                    stackWidth, stack.getMaxMutations());
            stackView.setLayoutX(index * stackWidth);
            root.getChildren().add(stackView);
        }
        return root;
    }
}
