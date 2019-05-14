package jestures.core.file;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.Paths;


class FileManagerTest {
    private static final String USER_FOLDER = "BUBINO";

    @BeforeAll
    static void setUp() {
        Assertions.assertDoesNotThrow(FileManager::createKinectNativeFolderLib);
    }

    @AfterAll
    static void tearDown() {
        Assertions.assertDoesNotThrow(() -> FileManager.deleteUserFolder(FileManagerTest.USER_FOLDER));
    }

    @Test
    @DisplayName("A File Manager can create a user")
    void createUserFolder() {
        Assertions.assertDoesNotThrow(() -> FileManager.createUserFolder(FileManagerTest.USER_FOLDER));
    }

    @Test
    @DisplayName("A File Manager can get all the users folders")
    void getAllUserFolder() {
        Assertions.assertDoesNotThrow(FileManager::getAllUserFolder);
    }

    @Test
    @DisplayName("A File Manager can get the user created user")
    void getUserDir() {
        try {
            Assertions.assertEquals(FileManagerTest.USER_FOLDER, Paths.get(FileManager.getUserDir(FileManagerTest.USER_FOLDER))
                    .getFileName()
                    .toString());
        } catch (FileNotFoundException e) {
            e.getMessage();
        }
    }
}
