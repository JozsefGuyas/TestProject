
package com.gj.testproject.helper;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileProcessingInformation {
    
    private File file;
    
    private boolean isUnderProcess;
    
    private FileTypes type;
    
}
