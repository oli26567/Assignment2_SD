package com.streaming.pipeline.phases.ingest;

import com.streaming.pipeline.core.Task;
import com.streaming.pipeline.core.ValidationResult;
import com.streaming.pipeline.models.MasterFile;

public class FormatValidatorTask implements Task {

    @Override
    public String getName() {
        return "Format Validator";
    }

    @Override
    public ValidationResult execute(MasterFile file) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffprobe", "-v", "error", 
                    "-show_entries", "format=format_name", 
                    "-of", "default=noprint_wrappers=1:nokey=1", 
                    file.getFilename()
            );
            Process process = pb.start();
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
            String nativeFormat = reader.readLine();
            process.waitFor();
            
            if (nativeFormat != null) {
                if (!nativeFormat.toLowerCase().contains("mp4") && 
                    !nativeFormat.toLowerCase().contains("mov") && 
                    !nativeFormat.toLowerCase().contains("matroska") && 
                    !nativeFormat.toLowerCase().contains("webm")) {
                    return new ValidationResult(false, "Unsupported hardware format: " + nativeFormat);
                }
            } else {
                return new ValidationResult(false, "FFprobe could not detect a valid video container.");
            }
        } catch (Exception e) {
            return new ValidationResult(false, "Failed to run native ffprobe format extraction: " + e.getMessage());
        }
        
        return new ValidationResult(true, "Format is valid studio specification.");
    }
}
