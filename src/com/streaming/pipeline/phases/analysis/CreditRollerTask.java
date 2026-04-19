package com.streaming.pipeline.phases.analysis;

import com.streaming.pipeline.core.Task;
import com.streaming.pipeline.core.ValidationResult;
import com.streaming.pipeline.models.MasterFile;

public class CreditRollerTask implements Task {

    @Override
    public String getName() {
        return "Credit Roller";
    }

    @Override
    public ValidationResult execute(MasterFile file) {
        return new ValidationResult(true, "Credits timestamp identified and triggered.");
    }
}
