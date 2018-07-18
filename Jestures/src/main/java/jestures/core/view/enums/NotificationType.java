package jestures.core.view.enums;

import eu.hansolo.enzo.notification.Notification;

/**
 * Enum for selecting type for {@link Notification}.
 */
public enum NotificationType {
    /**
     * The different types of the {@link Notification}.
     */
    ERROR(1), WARNING(2), SUCCESS(3), INFO(4), DEFAULT(0);

    private int dim;

    NotificationType(final int size) {
        this.dim = size;
    }

    /**
     * Get the dim.
     *
     * @return the dim
     */
    public int getValue() {
        return this.dim;
    }

    /**
     * Enum for popup duration{@link Duration}.
     *
     */
    public enum Duration {
        /**
         * Dims.
         */
        VERY_VERY_LONG(10), VERY_LONG(7), LONG(5), MEDIUM(3), SHORT(1), SHORTER(0.5), SHORTEST(0.3);

        private double dim;

        Duration(final double size) {
            this.dim = size;
        }

        /**
         * Get the dim.
         *
         * @return the dim
         */
        public double getValue() {
            return this.dim;
        }
    }

}
