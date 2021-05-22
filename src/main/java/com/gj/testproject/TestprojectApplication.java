
package com.gj.testproject;

import com.gj.testproject.helper.CliOptions;
import com.gj.testproject.helper.Configuration;
import com.gj.testproject.task.FolderListenerTask;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@Log4j2
public class TestprojectApplication {
    
    private static final int MINIMUM_THREAD_NUMBER = 1;
    
    private static final int MINIMUM_IDLE_TIMEOUT = 1;
    
    @Getter
    private final Options options = new Options();
    
    public static void main(String[] args) throws ParseException {
       
        var application = new TestprojectApplication();
        application.init();
        
        if (application.notContainsIncomingFolderPath(args)) {
            
            var formatter = new HelpFormatter();
            formatter.printHelp( "ant", application.getOptions() );
            log.error("Missing required option: -i");
            System.exit(1);
        }
        
        var configuration = application.createConfiguration(args);
        
        var folderListenerTask = new FolderListenerTask(configuration);
        folderListenerTask.start();
        
    }
   
    private void init() {
      
        options.addOption(Option.builder(CliOptions.FOLDER_PATH.getOption()).argName("folder path").hasArg().desc("Incoming folder path").build());
        options.addOption(Option.builder(CliOptions.IDLE_TIMEOUT.getOption()).argName("sec").hasArg().desc("Idle timeout as sec").build());
        options.addOption(Option.builder(CliOptions.NUMBER_OF_THREAD.getOption()).argName("number of thread").hasArg().desc("Number of working thread").build());
        options.addOption(Option.builder(CliOptions.REPEAT_COUNT.getOption()).argName("count").hasArg().desc("Number of repeat word pair count").build());

    }
    
    private boolean notContainsIncomingFolderPath(String[] args) throws ParseException {
        
        var parser = new DefaultParser();
        
        var cmd = parser.parse( options, args, true);
        
        return !cmd.hasOption(CliOptions.FOLDER_PATH.getOption());
        
    }
    
    private Configuration createConfiguration(String[] args) throws ParseException {
        
        var configuration = new Configuration();       
        var parser = new DefaultParser();
        var cmd = parser.parse( options, args, true);
       
        if (cmd.hasOption(CliOptions.FOLDER_PATH.getOption())) {
            configuration.setIncommingFolderPath(cmd.getOptionValue(CliOptions.FOLDER_PATH.getOption()));
        }
        
        if (cmd.hasOption(CliOptions.IDLE_TIMEOUT.getOption())) {
            var idleTimeout = cmd.getOptionValue(CliOptions.IDLE_TIMEOUT.getOption());
            if (isInteger(idleTimeout)) {
                var timeout = Integer.parseInt(idleTimeout);
                if (timeout < MINIMUM_IDLE_TIMEOUT) {
                    log.warn(CliOptions.IDLE_TIMEOUT.getOption() + " option can not be less than 1");
                    log.warn("Use default value: " + configuration.getIdleTimeoutSec());
                } else {
                    configuration.setIdleTimeoutSec(timeout);               
                    
                }
                        
            } else {
                log.warn(CliOptions.IDLE_TIMEOUT.getOption() + " option is not number!");
                log.warn("Use default value: " + configuration.getIdleTimeoutSec());
            }
                    
        }
        
        if (cmd.hasOption(CliOptions.NUMBER_OF_THREAD.getOption())) {
            var numberOfThread = cmd.getOptionValue(CliOptions.NUMBER_OF_THREAD.getOption());
            if (isInteger(numberOfThread)) {
                var thread = Integer.parseInt(numberOfThread);
                if (thread < MINIMUM_THREAD_NUMBER) {
                    log.warn(CliOptions.NUMBER_OF_THREAD.getOption() + " option can not be less than 1");
                    log.warn("Use default value: " + configuration.getWorkingThreadCount());
                } else {
                    configuration.setWorkingThreadCount(thread);               
                    
                }
            } else {
                log.warn(CliOptions.NUMBER_OF_THREAD.getOption() + " option is not number!");
                log.warn("Use default value: " + configuration.getWorkingThreadCount());
            }
            
        }
        
        if (cmd.hasOption(CliOptions.REPEAT_COUNT.getOption())) {
            var repeatCount = cmd.getOptionValue(CliOptions.REPEAT_COUNT.getOption());
            if (isInteger(repeatCount)) {
              configuration.setWordpairRepeateCount(Integer.parseInt(repeatCount));
            } else {
                log.warn(CliOptions.REPEAT_COUNT.getOption() + " option is not number!");
                log.warn("Use default value: " + configuration.getWordpairRepeateCount());
            }
            
        }
        
        return configuration;
    }
    
    private boolean isInteger(String number) {
        try {
            int i = Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            log.debug(e.getMessage(), e);
            return false;
        }
    }
    
}
