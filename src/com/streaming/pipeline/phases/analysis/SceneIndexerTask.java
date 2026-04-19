package com.streaming.pipeline.phases.analysis;

import com.streaming.pipeline.core.Task;
import com.streaming.pipeline.core.ValidationResult;
import com.streaming.pipeline.models.MasterFile;

public class SceneIndexerTask implements Task {

    @Override
    public String getName() {
        return "Scene Indexer";
    }

    @Override
    public ValidationResult execute(MasterFile file) {
        
        
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg", 
                    "-i", file.getFilename(),
                    "-t", "30", 
                    "-filter:v", "select='gt(scene,0.4)',showinfo", 
                    "-f", "null", 
                    "-"
            );
            
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
            String line;
            
            java.util.List<String> timestamps = new java.util.ArrayList<>();
            boolean foundCut = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Parsed_showinfo") && line.contains("pts_time:")) {
                    int startIndex = line.indexOf("pts_time:") + 9;
                    int endIndex = line.indexOf(" ", startIndex);
                    if(endIndex == -1) endIndex = line.length();
                    
                    String timestamp = line.substring(startIndex, endIndex);
                    timestamps.add(timestamp);
                    foundCut = true;
                }
            }
            
            process.waitFor();
            
            if (!foundCut) {
            }
            
            new java.io.File("movie_101/metadata").mkdirs();
            StringBuilder json = new StringBuilder("[\n");
            for(int k=0; k<timestamps.size(); k++) {
                json.append("  {\n    \"timestamp\": \"").append(timestamps.get(k)).append("\",\n")
                    .append("    \"type\": \"SCENE_CHANGE_DETECTED\"\n  }");
                if(k < timestamps.size()-1) json.append(",");
                json.append("\n");
            }
            json.append("]\n");
            java.nio.file.Files.writeString(new java.io.File("movie_101/metadata/scene_analysis.json").toPath(), json.toString());
            
        } catch (Exception e) {
            return new ValidationResult(false, "Failed to run native ffmpeg for scene indexing: " + e.getMessage());
        }
        
        return new ValidationResult(true, "Finished parsing scenes.");
    }
}
