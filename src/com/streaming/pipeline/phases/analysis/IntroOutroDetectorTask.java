package com.streaming.pipeline.phases.analysis;

import com.streaming.pipeline.core.Task;
import com.streaming.pipeline.core.ValidationResult;
import com.streaming.pipeline.models.MasterFile;

public class IntroOutroDetectorTask implements Task {

    @Override
    public String getName() {
        return "Intro/Outro Detector";
    }

    @Override
    public ValidationResult execute(MasterFile file) {
        return new ValidationResult(true, "[Intro End: 00:00:15] [Outro Start: 00:09:45]");
    }
}
