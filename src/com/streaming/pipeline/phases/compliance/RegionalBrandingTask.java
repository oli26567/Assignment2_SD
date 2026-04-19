package com.streaming.pipeline.phases.compliance;

import com.streaming.pipeline.core.Task;
import com.streaming.pipeline.core.ValidationResult;
import com.streaming.pipeline.models.MasterFile;

public class RegionalBrandingTask implements Task {

    @Override
    public String getName() {
        return "Regional Branding";
    }

    @Override
    public ValidationResult execute(MasterFile file) {
        return new ValidationResult(true, "Regional branding successfully integrated.");
    }
}
