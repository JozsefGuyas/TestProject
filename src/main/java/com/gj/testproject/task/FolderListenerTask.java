
package com.gj.testproject.task;

import com.gj.testproject.helper.Configuration;
import com.gj.testproject.helper.FileHolder;
import com.gj.testproject.helper.FileTypes;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Text;

@Log4j2
@RequiredArgsConstructor
public class FolderListenerTask extends TimerTask implements Task{
    
    private static final int DELAY = 0;
    
    private static final int PERIOD = 500;
    
    private static final String TASK_NAME = "FolderListener";
    
    private final Configuration configuration;
    
    private File incomingFolder;
    
    private Timer timer;
    
    private FileHolder fileHolder = new FileHolder(); 
    
    private ExecutorService executorService;
    
    public void start() {
        
        if (isTimerNotRunning(timer)) {
            timer = new Timer(TASK_NAME);        
            timer.schedule(this, DELAY, PERIOD);
            
            incomingFolder = new File(configuration.getIncommingFolderPath());
            if (isFolderNotExist(incomingFolder)) {
                createFolder(incomingFolder);
            }
            executorService = Executors.newFixedThreadPool(configuration.getWorkingThreadCount());
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
    
    private boolean textFileFilter(File file) {
        return file.getName().endsWith("txt");
    }
    
    private boolean imageFileFilter(File file) {
        return  file.getName().endsWith("bmp")
                || file.getName().endsWith("gif")
                || file.getName().endsWith("png")
                || file.getName().endsWith("jpg");
    }
    
    private void onFileprocessFinished(File file) {
        
    }
    

    @Override
    public void run() {
        
        var textFiles = incomingFolder.listFiles(this::textFileFilter);
        
        var imageFiles = incomingFolder.listFiles(this::imageFileFilter);
        
        fileHolder.addFiles(textFiles, FileTypes.TEXT_FILE);
        
        fileHolder.addFiles(imageFiles, FileTypes.IMAGE_FILE);
        
        for (var file : textFiles) {
            if (fileHolder.isFileNotUnderProcess(file)) {
                fileHolder.setUnderProcess(file, true);
                TexthandlerTask texthandlerTask = new TexthandlerTask(file, configuration.getWordpairRepeateCount());
                texthandlerTask.setOnProcessFinishedListener(this::onFileprocessFinished);
                executorService.execute(texthandlerTask);
            }
        }
        
    }

  
}
