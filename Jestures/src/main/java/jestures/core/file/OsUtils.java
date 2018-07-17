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

package jestures.core.file;

import java.util.Locale;

/**
 *
 *
 */
final class OsUtils {

    private OsUtils() {

    }

    /**
     * types of Operating Systems.
     */
    public enum OSType {
        /**
         * Systems.
         */
        Windows, MacOS, Linux, Other
    };

    // cached result of OS detection
    private static OSType detectedOS; // NOPMD
    private static String separator; // NOPMD

    /**
     * detect the operating system from the os.name System property and cache the result.
     *
     * @return the operating system detected
     */
    public static OSType getOperatingSystemType() {
        if (OsUtils.detectedOS == null) {
            final String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if (os.indexOf("mac") >= 0 || os.indexOf("darwin") >= 0) {
                OsUtils.detectedOS = OSType.MacOS;
            } else if (os.indexOf("win") >= 0) {
                OsUtils.detectedOS = OSType.Windows;
            } else if (os.indexOf("nux") >= 0) {
                OsUtils.detectedOS = OSType.Linux;
            } else {
                OsUtils.detectedOS = OSType.Other;
            }
        }
        return OsUtils.detectedOS;
    }

    /**
     * Get the system separator.
     *
     * @return the separator
     */
    public static String getSeparator() {
        if (OsUtils.separator == null) {
            OsUtils.separator = System.getProperty("file.separator");
        }
        return OsUtils.separator;
    }
}
