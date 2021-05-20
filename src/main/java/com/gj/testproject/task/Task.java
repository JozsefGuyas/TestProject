
package com.gj.testproject.task;

import java.io.File;


public interface Task {
    default boolean isFolderNotExist(File folder) {
        return !folder.exists();
    }
}
