
package com.gj.testproject.helper;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class FileHolder {
    
    ConcurrentHashMap<String, FileData> fileHolder = new ConcurrentHashMap<>();
    
    public void addFiles(File[] files, FileTypes type) {
        for (var file : files) {
            if (!fileHolder.containsKey(file.getName())) {
                fileHolder.put(file.getName(), new FileData(file, false, type));
            }
        }
    }
    
    public void setUnderProcess(File file, boolean isUnderProcess) {
        if (fileHolder.containsKey(file.getName())) {
            
            var fileData = fileHolder.get(file.getName());
            fileData.setUnderProcess(isUnderProcess);
            
        }
    }
    
    public boolean isFileUnderProcess(File file) {
        if (fileHolder.containsKey(file.getName())) {
            var fileData = fileHolder.get(file.getName());
            return fileData.isUnderProcess();
        }
        return false;
    }
    
    public boolean isFileNotUnderProcess(File file) {
        if (fileHolder.containsKey(file.getName())) {
            var fileData = fileHolder.get(file.getName());
            return !fileData.isUnderProcess();
        }
        return false;
    }
    
    public void remove(File file) {
        if (fileHolder.containsKey(file.getName())) {
            fileHolder.remove(file.getName());          
        }
    }
    
}
