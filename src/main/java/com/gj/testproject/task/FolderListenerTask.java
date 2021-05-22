
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
    
    private IdleTimerTask idleTimerTask = new IdleTimerTask();
    
    public void start() {
        
        if (isTimerNotRunning(timer)) {
            
            incomingFolder = new File(configuration.getIncommingFolderPath());
            if (isFolderNotExist(incomingFolder)) {
                createFolder(incomingFolder);
            }
            executorService = Executors.newFixedThreadPool(configuration.getWorkingThreadCount());
            
            idleTimerTask.setOnIdleTimerListener(this::idleTimeout);
            idleTimerTask.start(configuration.getIdleTimeoutSec());
            
            timer = new Timer(TASK_NAME);        
            timer.schedule(this, DELAY, PERIOD);
            
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
            log.error("Can not create folder! Invalid folder path or invalid format! Try these formats: ./example, ../example, /home/user/example, C://example");   
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
    
    private void idleTimeout() {
        log.info("idle timeout exit the program!");
        executorService.shutdown();
        System.exit(0);
    }
    
    private void onFileprocessFinished(File file) {
        fileHolder.remove(file);
        log.info("File process finished: " + file.getName());
    }
    
    @Override
    public void run() {
        
        var textFiles = incomingFolder.listFiles(this::textFileFilter);
        
        var imageFiles = incomingFolder.listFiles(this::imageFileFilter);
        
        fileHolder.addFiles(textFiles, FileTypes.TEXT_FILE);
        
        fileHolder.addFiles(imageFiles, FileTypes.IMAGE_FILE);
        
        fileHolder.getEntrys().forEach((key, value) -> {
            Task task = null;
            if (fileHolder.isFileNotUnderProcess(value.getFile())) {
                if (value.getType().equals(FileTypes.TEXT_FILE)) {
                
                    fileHolder.setUnderProcess(value.getFile(), true);
                    task = new TexthandlerTask(value.getFile(), configuration.getWordpairRepeateCount());
                   
                }
                
                if (value.getType().equals(FileTypes.IMAGE_FILE)) {
                    fileHolder.setUnderProcess(value.getFile(), true);
                    task = new ImageHandlerTask(value.getFile());
                }
            }
            if (task != null) {
                task.setOnProcessFinishedListener(this::onFileprocessFinished);
                executorService.execute(task);
                idleTimerTask.reset();
            }
            
        });
      
    }

  
}
