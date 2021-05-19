
package com.gj.testproject.helper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Configuration {
    
    private String incommingFolderPath;
    
    private int wordpairRepeateCount = 3;
    
    private int workingThreadCount = 1;
    
    private int idleTimeoutSec = 60;
    
}
