/*******************************************************************************
 * Copyright (c) 2018 Giulianini Luca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package jestures.core.view.enums;

import eu.hansolo.enzo.notification.Notification;

/**
 * Enumeration for notification type.
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
        VERY_VERY_LONG(10), VERY_LONG(7), LONG(4), MEDIUM(2), SHORT(1), SHORTER(0.5), SHORTEST(0.3);

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
