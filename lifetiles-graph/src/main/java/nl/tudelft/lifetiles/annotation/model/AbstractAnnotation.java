package nl.tudelft.lifetiles.annotation.model;

/**
 * Abstract class for a domain annotation.
 * Has a start and end coordinate to the reference sequence.
 *
 * @author Jos
 *
 */
public abstract class AbstractAnnotation extends AbstractBookmark {

    /**
     * End coordinate of the annotation domain.
     */
    private final long end;

    /**
     * Construct a domain annotation.
     *
     * @param start
     *            Start coordinate for the annotation domain.
     * @param end
     *            End coordinate for the annotation domain.
     */
    public AbstractAnnotation(final long start, final long end) {
        super(start);
        this.end = end;
    }

    /**
     * @return the end
     */
    public long getGenomeEndPosition() {
        return end;
    }
}
