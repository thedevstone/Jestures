package jestures.core.view;

/**
 * Enum for text. You can set all the dimensions.
 *
 */
public enum IconDim {
    /**
     * Dims.
     */
    BIGGEST(80), BIGGER(60), BIG(40), MEDIUM(30), SMALL(20), SMALLER(15), SMALLEST(10);

    private int dim;

    IconDim(final int size) {
        this.dim = size;
    }

    /**
     * Get the dim.
     *
     * @return the dim
     */
    public int getDim() {
        return this.dim;
    }
}