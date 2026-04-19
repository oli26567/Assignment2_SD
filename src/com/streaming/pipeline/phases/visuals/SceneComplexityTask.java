package com.streaming.pipeline.phases.visuals;

import com.streaming.pipeline.core.Task;
import com.streaming.pipeline.core.ValidationResult;
import com.streaming.pipeline.models.MasterFile;

public class SceneComplexityTask implements Task {

    @Override
    public String getName() {
        return "Scene Complexity";
    }

    @Override
    public ValidationResult execute(MasterFile file) {
        
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffprobe", 
                    "-v", "error", 
                    "-show_entries", "format=duration,size,bit_rate", 
                    "-of", "default=noprint_wrappers=1:nokey=1", 
                    file.getFilename()
            );
            Process process = pb.start();
            
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
            String dur = reader.readLine();
            String size = reader.readLine();
            String rate = reader.readLine();
            
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                 return new ValidationResult(false, "ffprobe process failed with exit code: " + exitCode);
            }
            
            long bytes = Long.parseLong(size);
            String durTrunc = dur.substring(0, Math.min(4, dur.length()));
            return new ValidationResult(true, "[FFprobe: " + (bytes/1024/1024) + "MB file, " + durTrunc + "sec duration]");
        } catch (Exception e) {
            return new ValidationResult(false, "Failed to run native ffprobe command: " + e.getMessage());
        }
    }
}
