
package com.gj.testproject.task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
@RequiredArgsConstructor
public class TexthandlerTask implements Runnable, Task{
    
    private static final String DESTINATION_DONE_FOLDER = "/text-done/";
    
    private static final String DESTINATION_ERROR_FOLDER = "/text-error/";
    
    private final File textFile;
    
    private final int maxWordPair;
    
    private TaskEventListener taskEventListener;

    @Override
    public void run() {
        try {
            
            var message = Files.readString(textFile.toPath());
            
            var wordpair = findWordPairs(message);
            
            var selectedWordPairs = selectWordPairs(wordpair, maxWordPair);
            
            var sortedWordPairs = sortWordPairs(selectedWordPairs);
            
            var wordpairAsText = createTextFromResult(sortedWordPairs);
            
            var destFolder = new File(textFile.getParent()+ DESTINATION_DONE_FOLDER);
            
            var destinationFile = new File(textFile.getParent() + DESTINATION_DONE_FOLDER + textFile.getName());
            
            if (isFolderNotExist(destFolder)) {
                destFolder.mkdirs();
            }
            
            Files.write(destinationFile.toPath(),
                    wordpairAsText.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.SYNC);
            
            Files.delete(textFile.toPath());
            
            fireOnProcessFinishedListener(textFile);
            
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
    
    private Map<String, Integer> findWordPairs(String message) {
        
        var split = message.split(" ");
        var wordpairs = new HashMap<String, Integer>();
        var count = 0;

        for (int i = 0; i < split.length - 1; i++) {
            var temp = split[i] + " " + split[i + 1];
            temp = temp.toLowerCase();
            if (message.toLowerCase().contains(temp)) {
                if (wordpairs.containsKey(temp)) {
                    wordpairs.put(temp, wordpairs.get(temp) + 1);
                }
                else {
                    wordpairs.put(temp, 1);
                }
            }

        }
        return wordpairs;
    }
    
    private Map<String, Integer> selectWordPairs(Map<String, Integer> wordPairs, int count) {
        return wordPairs.entrySet().stream()
                .filter((map) -> map.getValue() > count)
                .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
    }
    
    private Map<String, Integer> sortWordPairs(Map<String, Integer> wordPairs) {
        return wordPairs.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue())
                .collect(Collectors.toMap(e -> e.getKey(),e -> e.getValue()));
    }
    
    private String createTextFromResult(Map<String, Integer> map) {
        var sb = new StringBuilder();
        map.forEach((word, count) -> {
            sb.append(word).append(" : ").append(count).append(System.lineSeparator());
        });
        return sb.toString();
    }
    
    public void setOnProcessFinishedListener(TaskEventListener listener) {
        this.taskEventListener = listener;
    }
    
    private void fireOnProcessFinishedListener(File file) {
        if (this.taskEventListener != null) {
            this.taskEventListener.onProcessFinished(file);
        }
    }
    
}
