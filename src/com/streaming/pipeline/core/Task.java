package com.streaming.pipeline.core;

import com.streaming.pipeline.models.MasterFile;

public interface Task {
    String getName();

    ValidationResult execute(MasterFile file);
}
