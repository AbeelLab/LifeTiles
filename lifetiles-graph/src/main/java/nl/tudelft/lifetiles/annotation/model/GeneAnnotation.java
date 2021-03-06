package nl.tudelft.lifetiles.annotation.model;

import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import nl.tudelft.lifetiles.sequence.model.Sequence;
import nl.tudelft.lifetiles.sequence.model.SequenceSegment;

/**
 * Annotation of a gene onto the graph.
 *
 * @author Jos
 *
 */
public class GeneAnnotation extends AbstractAnnotation {

    /**
     * Name of the gene.
     */
    private final String name;

    /**
     * The segments to which this annotation maps to.
     */
    private Set<SequenceSegment> mappingSegments;

    /**
     * Construct a gene annotation.
     *
     * @param start
     *            Start coordinate of the domain annotation.
     * @param end
     *            End coordinate of the domain annotation.
     * @param name
     *            Name of the gene.
     */
    public GeneAnnotation(final long start, final long end, final String name) {
        super(start, end);
        this.name = name;
        mappingSegments = new HashSet<>();
    }

    /**
     * @return name of the gene.
     */
    public String getName() {
        return name;
    }

    /**
     * Maps known mutation onto a set of segments.
     *
     * @param segments
     *            Segments to map the annotation to.
     * @param reference
     *            The current reference used in the list of segments.
     * @return segments which annotation should be mapped to.
     */
    public Set<SequenceSegment> mapOntoSequence(
            final Set<SequenceSegment> segments, final Sequence reference) {
        mappingSegments = new HashSet<>();
        for (SequenceSegment segment : segments) {
            if (segment.getSources().contains(reference)
                    && segment.getStart() <= getGenomeEndPosition()
                    && segment.getEnd() > getGenomePosition()) {
                mappingSegments.add(segment);
            }
        }
        return mappingSegments;
    }

    /**
     * Returns the String representation for the gene annotation to be displayed
     * in the tooltip of it's bookmark.
     *
     * @return
     *         Tooltip string representation.
     */
    public String toString() {
        Formatter formatter = new Formatter();
        formatter
                .format("Gene Name: %1$s%nGene Start Position: %2$s%nGene End Position: %3$s",
                        name, getGenomePosition(), getGenomeEndPosition());
        String geneAnnotation = formatter.toString();
        formatter.close();
        return geneAnnotation;
    }

    /**
     * Method which return the unified position of the bookmark in the
     * unified graph.
     *
     * @return unified position of the bookmark in the graph.
     */
    @Override
    public long getUnifiedPosition() {
        long position = 1;
        for (SequenceSegment segment : mappingSegments) {
            position = Math.max(position, segmentPosition(segment));
        }
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toCellString() {
        return "Gene: " + name;
    }

}
