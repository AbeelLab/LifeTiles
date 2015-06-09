package nl.tudelft.lifetiles.tree.view;


import javafx.geometry.Bounds;
import javafx.scene.control.Control;
import javafx.scene.input.MouseButton;
import nl.tudelft.lifetiles.tree.controller.TreeController;
import nl.tudelft.lifetiles.tree.model.PhylogeneticTreeItem;


/**
 * A View to display a tree.
 * The tree will be displayed in a circle.
 *
 * @author Albert Smit
 *
 */
public class SunburstView extends Control {

    /**
     * The root of the tree this view will show.
     */
    private PhylogeneticTreeItem rootItem;
    /**
     * the current node we use as the center of the view.
     */
    private PhylogeneticTreeItem currentItem;

    /**
     * the center X coordinate of the view.
     */
    private double centerX;
    /**
     * the center Y coordinate of the view.
     */
    private double centerY;
    /**
     * the {@link TreeController} controlling this SunburstView.
     */
     private TreeController controller;
     /**
     * the scaling factor to calculate coordinates, starts at 1.
     */
    private double scale = 1d;
    /**
     * The bounds for this view, used to scale content to fit.
     */
    private Bounds parentLayoutBounds;


    /**
     * Creates a new SunburstView.
     */
    public SunburstView() {
        super();
        centerX = getWidth() / 2d;
        centerY = getHeight() / 2d;
        this.setOnScroll((scrollEvent) -> {
            double zoomFactor = 1d / scrollEvent.getDeltaY();
            scale = scale + zoomFactor;
            update();
        });
    }

    /**
     * Creates a new SunburstView.
     *
     * @param root
     *            the root of the tree to display
     */
    public SunburstView(final PhylogeneticTreeItem root) {
        super();

        centerX = getWidth() / 2d;
        centerY = getHeight() / 2d;
        rootItem = root;
        selectNode(rootItem);

        this.setOnScroll((scrollEvent) -> {
            double zoomFactor = 1d / scrollEvent.getDeltaY();
            scale = scale + zoomFactor;
            update();
        });

    }

    /**
     * changes the currently selected node.
     *
     * @param selected
     *            the new selected node.
     */
    public final void selectNode(final PhylogeneticTreeItem selected) {
        if (selected != null) {
            currentItem = selected;
            scale = calculateScale();
            update();
        }

    }

    /**
     * Changes the displayed tree.
     *
     * @param root
     *            the new root
     */
    public final void setRoot(final PhylogeneticTreeItem root) {
        rootItem = root;
        selectNode(rootItem);
    }

    /**
     * @param controller
     *            the {@link TreeController} controlling this tree
     */
    public final void setController(final TreeController controller) {
        this.controller = controller;
    }
    /**
     * stores a reference to this nodes' parents bounds
     * because the nodes' own bounds are not accurate.
     * @param bounds
     *              The bounds of the parent node
     */
    public final void setBounds(final Bounds bounds) {
        parentLayoutBounds = bounds;
        if(rootItem != null) {
            scale = calculateScale();
            update();
        }
    }

    /**
     * updates the view by redrawing all elements.
     */
    private void update() {
        // remove the old elements
        getChildren().clear();

        centerX = getWidth() / 2d;
        centerY = getHeight() / 2d;

        // add a center unit
        SunburstCenter center = new SunburstCenter(currentItem, scale);
        center.setOnMouseClicked((mouseEvent) -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                selectNode(currentItem.getParent());
                controller.shoutVisible(currentItem.getChildSequences());
           }
        });
        getChildren().add(center);

        // add the ring units
        double totalDescendants = currentItem.numberDescendants();
        double degreeStart = 0;
        for (PhylogeneticTreeItem child : currentItem.getChildren()) {
            double sectorSize = (child.numberDescendants() + 1)
                    / totalDescendants;
            double degreeEnd = degreeStart
                    + (AbstractSunburstNode.CIRCLEDEGREES * sectorSize);

            drawRingRecursive(child, 0, degreeStart, degreeEnd);
            degreeStart = degreeEnd;
        }
    }

    /**
     * draws all ringUnits.
     *
     * @param node
     *            the {@link PhylogeneticTreeItem} that this
     *            {@link SunburstRingSegment} will represent.
     * @param layer
     *            the layer on which this {@link SunburstRingSegment} is located
     * @param degreeStart
     *            the start point in degrees
     * @param degreeEnd
     *            the end point in degrees
     */
    private void drawRingRecursive(final PhylogeneticTreeItem node,
            final int layer, final double degreeStart, final double degreeEnd) {
        // generate ring
        SunburstRingSegment ringUnit = new SunburstRingSegment(node, layer,
                degreeStart, degreeEnd, centerX, centerY, scale);
        ringUnit.setOnMouseClicked((mouseEvent) -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                selectNode(node);
                controller.shoutVisible(currentItem.getChildSequences());
            }
        });
        getChildren().add(ringUnit);

        double totalDescendants = node.numberDescendants();
        double start = degreeStart;
        double sectorAngle = AbstractSunburstNode.calculateAngle(degreeStart,
                degreeEnd);

        // generate rings for child nodes
        for (PhylogeneticTreeItem child : node.getChildren()) {
            double sectorSize = (child.numberDescendants() + 1)
                    / totalDescendants;
            double end = start + (sectorAngle * sectorSize);

            drawRingRecursive(child, layer + 1, start, end);
            start = end;
        }

    }

    private double calculateScale() {
        int depth = currentItem.maxDepth();
        double minSize = Math.min(parentLayoutBounds.getWidth(), parentLayoutBounds.getHeight());

        double maxRadius = AbstractSunburstNode.CENTER_RADIUS;
        maxRadius += (depth * AbstractSunburstNode.RING_WIDTH);
        //maxRadius += AbstractSunburstNode.RING_WIDTH;

        double scale = minSize / (maxRadius * 2);
        if (scale > 1) {
            scale = 1d;
        } else  if (scale <= 0) { //should not be needed, but bounds returns 0 to often
            scale = 1d;
        }
        System.out.println(scale);
        return scale;
    }

}