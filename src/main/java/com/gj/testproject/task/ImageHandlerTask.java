
package com.gj.testproject.task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.style.Styler;

@Log4j2
@RequiredArgsConstructor
public class ImageHandlerTask implements Task {
    
    private static final String DESTINATION_DONE_FOLDER = "/image-done/";
    
    private static final String DESTINATION_ERROR_FOLDER = "/image-error/";
    
    private final File imageFile;
    
    private TaskEventListener taskEventListener; 

    @Override
    public void setOnProcessFinishedListener(TaskEventListener listener) {
        this.taskEventListener = listener;
    }
    
    private void fireOnProcessFinishedListener(File file) {
        if (this.taskEventListener != null) {
            this.taskEventListener.onProcessFinished(file);
        }
    }
    
    private String createFilename(String filename) {
        String[] splitFilename = filename.split("\\.");
        return splitFilename[0] + "-hist.bmp";
    }

    @Override
    public void run() {
        
        try {
            
            var destFolder = new File(imageFile.getParent() + DESTINATION_DONE_FOLDER);
        
            var series = new ImageProcessor().createHistogramFromImage(imageFile);
            
            log.debug(series.toString());
            
            var histogram = createHistogram(series);
            
            if (isFolderNotExist(destFolder)) {
                destFolder.mkdirs();
            }
            
            BitmapEncoder.saveBitmap(histogram, destFolder.getAbsolutePath() + "/" + createFilename(imageFile.getName()), BitmapEncoder.BitmapFormat.BMP);
                      
            Files.delete(imageFile.toPath());
            
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
        
        fireOnProcessFinishedListener(imageFile);
    }
    
    public CategoryChart createHistogram(List<Integer> series) {
 
        var min = Collections.min(series);
        var max = Collections.max(series);
        var bins = 100;//series.size();
        
        CategoryChart chart = new CategoryChartBuilder()
                .width(256)
                .height(100)
                .title("")
                .xAxisTitle("Grey Intensity")
                .yAxisTitle("Count")       
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setAvailableSpaceFill(.96);
        chart.getStyler().setOverlapped(true);
        chart.getStyler().setLegendVisible(false);
        //chart.getStyler().setXAxisLabelRotation(-90);
        chart.getStyler().setXAxisTicksVisible(false);
        chart.getStyler().setYAxisTicksVisible(false);
        
        Histogram histogram1 = new Histogram(series, bins, min, max);
        chart.addSeries("histogram", histogram1.getxAxisData(), histogram1.getyAxisData());
 
        return chart;
  }
        
    
}
