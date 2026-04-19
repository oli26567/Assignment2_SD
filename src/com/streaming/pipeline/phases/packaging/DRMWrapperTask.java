package com.streaming.pipeline.phases.packaging;

import com.streaming.pipeline.core.Task;
import com.streaming.pipeline.core.ValidationResult;
import com.streaming.pipeline.models.MasterFile;

public class DRMWrapperTask implements Task {

    @Override
    public String getName() {
        return "DRM Wrapper";
    }

    @Override
    public ValidationResult execute(MasterFile file) {
        return new ValidationResult(true, "[DRM Wrapper: AES-128]");
    }
}
