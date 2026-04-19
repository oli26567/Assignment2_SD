package com.streaming.pipeline.phases.ingest;

import com.streaming.pipeline.core.Task;
import com.streaming.pipeline.core.ValidationResult;
import com.streaming.pipeline.models.MasterFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class IntegrityCheckTask implements Task {

    @Override
    public String getName() {
        return "Integrity Check";
    }

    @Override
    public ValidationResult execute(MasterFile file) {
        try (InputStream is = Files.newInputStream(Paths.get(file.getFilename()))) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int readBytes;
            while ((readBytes = is.read(buffer)) > 0) {
                digest.update(buffer, 0, readBytes);
            }
            
            byte[] hash = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return new ValidationResult(true, "[Hash: " + hexString.toString().substring(0, 8) + "]");
        } catch (Exception e) {
            return new ValidationResult(false, "Native Checksum calculation failed: " + e.getMessage());
        }
    }
}
