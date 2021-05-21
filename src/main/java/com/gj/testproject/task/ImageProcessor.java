
package com.gj.testproject.task;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

public class ImageProcessor {
    
    public List<Integer> createHistogramFromImage(File file) throws IOException {
        
        var img = ImageIO.read(file);
 
        var grayScaleImage = toBlackAndWhite(img);
        
         //ImageIO.write(bwimg, "png", new File("/home/gj/example-bw.png"));
        
        return computeHistogram(grayScaleImage);
    }
    
    private int luminance(int rgb) {
        var r = (rgb >> 16) & 0xFF;
        var g = (rgb >> 8) & 0xFF;
        var b = rgb & 0xFF;
        return (r + b + g) / 3;
    }
 
    private BufferedImage toBlackAndWhite(BufferedImage image) {
       
        var width = image.getWidth();
        var height = image.getHeight();
        for(var i = 0; i < height; i++) {
            for(var j = 0; j < width; j++) {
                var c = new Color(image.getRGB(j, i));
                var red = (int)(c.getRed() * 0.299);
                var green = (int)(c.getGreen() * 0.587);
                var blue = (int)(c.getBlue() *0.114);
                var newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
                image.setRGB(j,i,newColor.getRGB());
            }
        }
        return image;
    }
 
    private List<Integer> computeHistogram(BufferedImage img) {
        var width = img.getWidth();
        var height = img.getHeight();
 
        var histo = new int[256];
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                histo[luminance(img.getRGB(x, y))]++;
            }
        }
        return Arrays.stream(histo).boxed().collect(Collectors.toList());
    }
 
    private int getMedian(int total, List<Integer> histo) {
        var median = 0;
        var sum = 0;
        for (var i = 0; i < histo.size() && sum + histo.get(i) < total / 2; i++) {
            sum += histo.get(i);
            median++;
        }
        return median;
    }
    
    
}
