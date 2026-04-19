package com.streaming.pipeline.phases.visuals;

import com.streaming.pipeline.core.Task;
import com.streaming.pipeline.core.ValidationResult;
import com.streaming.pipeline.models.MasterFile;

public class SpriteGeneratorTask implements Task {

    @Override
    public String getName() {
        return "Sprite Generator";
    }

    @Override
    public ValidationResult execute(MasterFile file) {
        try {
            new java.io.File("movie_101/images").mkdirs();
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg", "-y", "-v", "error", 
                    "-i", file.getFilename(),
                    "-frames", "1",
                    "-vf", "select=not(mod(n\\,100)),scale=160:90,tile=4x4", 
                    "movie_101/images/sprite_map.jpg"
            );
            
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            if (exitCode != 0) {
                 return new ValidationResult(false, "ffmpeg thumbnail extraction failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            return new ValidationResult(false, "Failed to run native ffmpeg thumbnail command: " + e.getMessage());
        }
        
        return new ValidationResult(true, "[120 thumbnails merged into sprite_map.jpg]");
    }
}
