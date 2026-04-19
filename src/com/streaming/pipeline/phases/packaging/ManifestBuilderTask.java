package com.streaming.pipeline.phases.packaging;

import com.streaming.pipeline.core.Task;
import com.streaming.pipeline.core.ValidationResult;
import com.streaming.pipeline.models.MasterFile;

import java.io.File;
import java.io.IOException;

public class ManifestBuilderTask implements Task {

    @Override
    public String getName() {
        return "Manifest Builder";
    }

    @Override
    public ValidationResult execute(MasterFile file) {
        try {
            File root = new File("movie_101");
            StringBuilder json = new StringBuilder("{\n  \"manifest_assets\": [\n");
            boolean[] isFirst = {true};
            
            buildManifest(root, json, isFirst);
            
            json.append("\n  ]\n}\n");
            java.nio.file.Files.writeString(new File("movie_101/manifest.json").toPath(), json.toString());
        } catch (IOException e) {
            return new ValidationResult(false, "Packaging failed due to Disk IO Error.");
        }
        
        return new ValidationResult(true, "Pipeline packaging complete. Ready for Global Release.");
    }
    private void buildManifest(File dir, StringBuilder json, boolean[] isFirst) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                buildManifest(f, json, isFirst);
            } else {
                if (!f.getName().equals("manifest.json")) {
                    if (!isFirst[0]) json.append(",\n");
                    json.append("    \"").append(f.getPath().replace("\\", "/")).append("\"");
                    isFirst[0] = false;
                }
            }
        }
    }
}
