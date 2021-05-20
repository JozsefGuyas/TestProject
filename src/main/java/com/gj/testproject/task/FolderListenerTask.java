
package com.gj.testproject.task;

import com.gj.testproject.helper.Configuration;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class FolderListenerTask extends TimerTask implements Task{
    
    private static final int DELAY = 0;
    
    private static final int PERIOD = 500;
    
    private static final String TASK_NAME = "FolderListener";
    
    private final Configuration configuration;
    
    private File incomingFolder;
    
    private Timer timer;
    
    public void start() {
        
        if (isTimerNotRunning(timer)) {
            timer = new Timer(TASK_NAME);        
            timer.schedule(this, DELAY, PERIOD);
            
            incomingFolder = new File(configuration.getIncommingFolderPath());
            if (isFolderNotExist(incomingFolder)) {
                createFolder(incomingFolder);
            }
            
            log.info(TASK_NAME + " started");
        }
        
    }
    
    public void stop() {
        if (isTimerRunning(timer)) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
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
    
    private boolean acceptedFiles(File file) {
        return file.getName().endsWith("txt") 
                || file.getName().endsWith("bmp")
                || file.getName().endsWith("gif")
                || file.getName().endsWith("png")
                || file.getName().endsWith("jpg");
    }

    @Override
    public void run() {
        var files = incomingFolder.listFiles(this::acceptedFiles);
        
        if (files.length > 0) {
            
        }
    }

  
}
