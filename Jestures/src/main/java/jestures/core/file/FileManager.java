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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

/**
 * Class dedicated to File managing.
 */
public final class FileManager {

    private static String libDir;
    private static final Logger LOG = Logger.getLogger(FileManager.class);

    /**
     * Constructor.
     */
    private FileManager() {
        // TODO Auto-generated constructor stub
    }
    // ##################################### FOLDER CREATION ################################

    private static boolean createDirectory(final String folder) throws IOException {
        if (!FileManager.checkIfFolderExists(folder)) {
            Files.createDirectories(Paths.get(folder));
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkIfFolderExists(final String folder) {
        return Files.exists(Paths.get(folder));
    }

    /**
     *
     * // ##################################### CREATE DIRECTORIES ################################ Create the framework
     * main directory.
     *
     * @throws IOException
     *             the {@link IOException}
     *
     */
    public static void createFrameworkDirectory() throws IOException {
        if (FileManager.libDir == null) {
            FileManager.libDir = OsUtils.getHomeFolder() + OsUtils.getSeparator() + LibPaths.LIB_NAME.getDirName();
            FileManager.createDirectory(FileManager.libDir);
            FileManager.createLibSubFolder(LibPaths.USER.getDirName());
        }
    }

    /**
     * @param folder
     *            the {@link String} path
     * @return <code>true</code> if the folder exists
     * @throws IOException
     *             the {@link IOException}
     */
    public static boolean createLibSubFolder(final String folder) throws IOException {
        final String tempPath = FileManager.libDir + OsUtils.getSeparator() + folder;
        return FileManager.createDirectory(tempPath);
    }

    /**
     * @param folder
     *            the {@link String} path
     * @return <code>true</code> if the folder exists
     * @throws IOException
     *             the {@link IOException}
     */
    public static boolean createUserFolders(final String folder) throws IOException {
        final String tempPath = FileManager.libDir + OsUtils.getSeparator() + LibPaths.USER.getDirName()
                + OsUtils.getSeparator() + folder.replaceAll("\\s+", "_");
        return FileManager.createDirectory(tempPath);
    }

    // ##################################### LOAD NATIVES ################################
    /**
     * Create the lib for native dll (Kinect).
     *
     * @throws IOException
     *             the {@link IOException}
     */
    public static void createKinectNativeFolderLib() {
        try {
            FileManager.createFrameworkDirectory();
            FileManager.createLibSubFolder(LibPaths.NATIVE_DIR.getDirName());
            FileManager.addDir(OsUtils.getHomeFolder() + OsUtils.getSeparator() + LibPaths.LIB_NAME.getDirName()
                    + OsUtils.getSeparator() + LibPaths.NATIVE_DIR.getDirName());
        } catch (final IOException e) {
            FileManager.LOG.error("Cannot load directory");
        }
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

    // ##################################### USEFUL METHODS ################################

    /**
     * Get the list of directorey.
     *
     * @return the {@link List} of directory
     * @throws IOException
     *             the {@link IOException}
     */
    public static List<String> getAllUserFolder() throws IOException {
        try (Stream<Path> paths = Files.walk(
                Paths.get(FileManager.libDir + OsUtils.getSeparator() + LibPaths.USER.getDirName()), 1)) {
            return paths.map(p -> p.getFileName().toString())
                        .filter(t -> !t.equals(LibPaths.USER.getDirName()))
                        .collect(Collectors.toList());
        }
    }

    /**
     * Get the user directory.
     *
     * @param name
     *            the {@link String} name.
     * @return the String path
     * @throws FileNotFoundException
     *             if the file is not found
     */
    public static String getUserDir(final String name) throws FileNotFoundException {
        final String userFolder = FileManager.libDir + OsUtils.getSeparator() + LibPaths.USER.getDirName()
                + OsUtils.getSeparator() + name + OsUtils.getSeparator();
        if (!FileManager.checkIfFolderExists(userFolder)) {
            throw new FileNotFoundException();
        } else {
            return userFolder;
        }
    }

}
