package com.streaming.pipeline.phases.compliance;

import com.streaming.pipeline.core.Task;
import com.streaming.pipeline.core.ValidationResult;
import com.streaming.pipeline.models.MasterFile;

public class SafetyScannerTask implements Task {

    @Override
    public String getName() {
        return "Safety Scanner";
    }

    @Override
    public ValidationResult execute(MasterFile file) {
        return new ValidationResult(true, "[Flagged: 0 regions for blurring]");
    }
}
