
package com.gj.testproject.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@AllArgsConstructor
public enum CliOptions {
    
    FOLDER_PATH("i"),
    REPEAT_COUNT("n"),
    NUMBER_OF_THREAD("t"),
    IDLE_TIMEOUT("d");
    
    @Getter
    private final String option;
}
