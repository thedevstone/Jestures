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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class dedicated to File managing.
 */
public final class FileManager {

    private static String libDir;

    /**
     * Constructor.
     */
    private FileManager() {
        // TODO Auto-generated constructor stub
    }

    private static void addDir(final String s) throws IOException {
        try {
            // This enables the java.library.path to be modified at runtime
            // From a Sun engineer at http://forums.sun.com/thread.jspa?threadID=707176
            //
            final Field field = ClassLoader.class.getDeclaredField("usr_paths");
            field.setAccessible(true);
            final String[] paths = (String[]) field.get(null);
            for (final String path : paths) {
                if (s.equals(path)) {
                    return;
                }
            }
            final String[] tmp = new String[paths.length + 1];
            System.arraycopy(paths, 0, tmp, 0, paths.length);
            tmp[paths.length] = s;
            field.set(null, tmp);
            System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + s);
        } catch (final IllegalAccessException e) {
            throw new IOException("Failed to get permissions to set library path");
        } catch (final NoSuchFieldException e) {
            throw new IOException("Failed to get field handle to set library path");
        }
    }

    /**
     * @param folder
     *            the {@link String} path
     * @return <code>true</code> if the folder exists
     */
    public static boolean createLibSubFolder(final String folder) {
        final String tempPath = FileManager.libDir + OsUtils.getSeparator() + folder.replaceAll("\\s+", "_");
        return FileManager.createDirectory(tempPath);
    }

    /**
     * Create the lib for native dll (Kinect).
     */
    public static void createKinectNativeFolderLib() {
        FileManager.createFrameworkDirectory();
        FileManager.createLibSubFolder(LibPaths.NATIVE_DIR.getDirName());
        try {
            FileManager.addDir(OsUtils.getHomeFolder() + OsUtils.getSeparator() + LibPaths.LIB_NAME.getDirName()
                    + OsUtils.getSeparator() + LibPaths.NATIVE_DIR.getDirName());
        } catch (final IOException e) {
            System.out.println("Cannot load the files");
        }
    }

    /**
     * Create the framework main directory.
     *
     */
    public static void createFrameworkDirectory() {
        if (FileManager.libDir == null) {
            FileManager.libDir = OsUtils.getHomeFolder() + OsUtils.getSeparator() + LibPaths.LIB_NAME.getDirName();
            FileManager.createDirectory(FileManager.libDir);
        }
    }

    private static boolean checkIfFolderExists(final String folder) {
        return Files.exists(Paths.get(folder));
    }

    private static boolean createDirectory(final String folder) {
        if (!FileManager.checkIfFolderExists(folder)) {
            try {
                Files.createDirectories(Paths.get(folder));
                return true;
            } catch (final Exception e) {
                System.out.println("Cannot create lib directory");
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Get the list of directorey.
     *
     * @return the {@link List} of directory
     * @throws IOException
     *             the {@link IOException}
     */
    public static List<String> getAllUserFolder() throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(FileManager.libDir), 1)) {
            return paths.map(p -> p.getFileName().toString())
                        .filter(t -> !t.equals(LibPaths.LIB_NAME.getDirName())
                                && !t.equals(LibPaths.NATIVE_DIR.getDirName()))
                        .collect(Collectors.toList());
        }
    }

}
