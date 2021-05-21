
package com.gj.testproject.task;

import java.io.File;


public interface Task extends Runnable{
    default boolean isFolderNotExist(File folder) {
        return !folder.exists();
    }
    
    default void setOnProcessFinishedListener(TaskEventListener listener) {
    }
}
