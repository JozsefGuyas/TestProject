
package com.gj.testproject;

import com.gj.testproject.helper.CliOptions;
import com.gj.testproject.helper.Configuration;
import com.gj.testproject.task.FolderListenerTask;
import java.io.File;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@Log4j2
public class TestprojectApplication {
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
                configuration.setIdleTimeoutSec(Integer.parseInt(idleTimeout));               
            } else {
                log.info(CliOptions.IDLE_TIMEOUT.getOption() + " option is not number!");
                log.info("Use default value: " + configuration.getIdleTimeoutSec());
            }
                    
        }
        
        if (cmd.hasOption(CliOptions.NUMBER_OF_THREAD.getOption())) {
            var numberOfThread = cmd.getOptionValue(CliOptions.NUMBER_OF_THREAD.getOption());
            if (isInteger(numberOfThread)) {
              configuration.setWorkingThreadCount(Integer.parseInt(numberOfThread));
            } else {
                log.info(CliOptions.NUMBER_OF_THREAD.getOption() + " option is not number!");
                log.info("Use default value: " + configuration.getWorkingThreadCount());
            }
            
        }
        
        if (cmd.hasOption(CliOptions.REPEAT_COUNT.getOption())) {
            var repeatCount = cmd.getOptionValue(CliOptions.REPEAT_COUNT.getOption());
            if (isInteger(repeatCount)) {
              configuration.setWordpairRepeateCount(Integer.parseInt(repeatCount));
            } else {
                log.info(CliOptions.REPEAT_COUNT.getOption() + " option is not number!");
                log.info("Use default value: " + configuration.getWordpairRepeateCount());
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
