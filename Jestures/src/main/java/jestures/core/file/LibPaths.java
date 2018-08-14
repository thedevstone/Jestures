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

/**
 * Native file paths.
 *
 */
public enum LibPaths {
    /**
     * Kinect1_64.
     */
    LIB_NAME(".Jestures"),
    /**
     * Native.
     */
    NATIVE_DIR("native"), USER("users");

    private String pathNative;

    LibPaths(final String nativeLib) {
        this.pathNative = nativeLib;
    }

    /**
     * Get the path for native.
     *
     * @return the String path
     */
    public String getDirName() {
        return this.pathNative;
    }
}
