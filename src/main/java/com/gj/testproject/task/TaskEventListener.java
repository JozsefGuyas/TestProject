
package com.gj.testproject.task;

import java.io.File;


public interface TaskEventListener {
    void onProcessFinished(File file);
}
