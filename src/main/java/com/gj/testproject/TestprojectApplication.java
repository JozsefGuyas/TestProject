
package com.gj.testproject;

import com.gj.testproject.helper.CliOptions;
import com.gj.testproject.helper.Configuration;
import java.util.List;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@Log4j2
public class TestprojectApplication {
    @Getter
    private final Options options = new Options();
    
    public static void main(String[] args) throws ParseException {
        
        TestprojectApplication application = new TestprojectApplication();
        application.init();
        
        if (application.notContainsIncomingFolderPath(args)) {
            
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "ant", application.getOptions() );
            log.error("Missing required option: -i");
            System.exit(1);
        }
        
        application.createConfiguration(args);
        
    }
    //szerda 9:00 
    private void init() {
      
        options.addOption(Option.builder(CliOptions.FOLDER_PATH.getOption()).argName("folder path").hasArg().desc("Incoming folder path").build());
        options.addOption(Option.builder(CliOptions.IDLE_TIMEOUT.getOption()).argName("idle timeout").hasArg().desc("Idle timeout as sec").build());
        options.addOption(Option.builder(CliOptions.NUMBER_OF_THREAD.getOption()).argName("number of thread").hasArg().desc("Number of working thread").build());
        options.addOption(Option.builder(CliOptions.REPEAT_COUNT.getOption()).argName("count").hasArg().desc("Number of repeat word pair count").build());

    }
    
    private boolean notContainsIncomingFolderPath(String[] args) throws ParseException {
        
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args, true);
        
        return !cmd.hasOption(CliOptions.FOLDER_PATH.getOption());
        
    }
    
    private Configuration createConfiguration(String[] args) throws ParseException {
        
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse( options, args, true);
        
        System.out.println("" + cmd.toString());
        
        return null;
    }
    
}
