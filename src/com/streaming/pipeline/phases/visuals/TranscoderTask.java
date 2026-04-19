package com.streaming.pipeline.phases.visuals;

import com.streaming.pipeline.core.Task;
import com.streaming.pipeline.core.ValidationResult;
import com.streaming.pipeline.models.MasterFile;

public class TranscoderTask implements Task {

    @Override
    public String getName() {
        return "Transcoder";
    }

    @Override
    public ValidationResult execute(MasterFile file) {
        try {
            new java.io.File("movie_101/video/h264").mkdirs();
            new java.io.File("movie_101/video/vp9").mkdirs();
            
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg", "-y", "-v", "error", 
                    "-i", file.getFilename(),
                    "-vf", "scale=-2:480",
                    "-c:v", "libx264",
                    "-preset", "ultrafast",
                    "-t", "2", 
                    "movie_101/video/h264/480p_sample.mp4"
            );
            pb.start().waitFor();
            
            ProcessBuilder pb2 = new ProcessBuilder(
                    "ffmpeg", "-y", "-v", "error", 
                    "-i", file.getFilename(),
                    "-vf", "scale=-2:480",
                    "-c:v", "libvpx-vp9",
                    "-deadline", "realtime",
                    "-t", "2", 
                    "movie_101/video/vp9/480p_sample.webm"
            );
            pb2.start().waitFor();
        } catch (Exception e) {
            return new ValidationResult(false, "Failed to run native ffmpeg command: " + e.getMessage());
        }
        
        return new ValidationResult(true, "Done transcoding!");
    }
}
