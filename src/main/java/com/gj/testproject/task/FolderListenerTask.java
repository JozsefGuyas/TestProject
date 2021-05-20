
package com.gj.testproject.task;

import com.gj.testproject.helper.Configuration;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class FolderListenerTask extends TimerTask{
    
    private final Configuration configuration;
    
    private static final int DELAY = 0;
    
    private static final int PERIOD = 500;
    
    private Timer timer;
    
    public void start() {
        
        if (isTimerNotRunning(timer)) {
            timer = new Timer("FolderListener");        
            timer.schedule(this, DELAY, PERIOD);
            
            File incomingFolder = new File(configuration.getIncommingFolderPath());
            if (isFolderNotExist(incomingFolder)) {
                createFolder(incomingFolder);
            }
        }
        
    }
    
    public void stop() {
        if (isTimerRunning(timer)) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }
    
    private boolean isFolderNotExist(File folder) {
        return !folder.exists();
    }
    
    private void createFolder(File folder) {
        if (!folder.mkdirs()) {
            log.error("Can not create folder! Invalid folder path! Accepted formats: ./example, ../example, /home/user/example");   
            System.exit(1);
        }
    }
    
    private boolean isTimerRunning(Timer timer) {
        return timer != null;
    }
    
    private boolean isTimerNotRunning(Timer timer) {
        return timer == null;
    }

    @Override
    public void run() {
        
    }
}
