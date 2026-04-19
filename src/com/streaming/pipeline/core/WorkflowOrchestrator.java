package com.streaming.pipeline.core;

import com.streaming.pipeline.models.MasterFile;
import java.util.ArrayList;
import java.util.List;

public class WorkflowOrchestrator {
    
    private final List<Task> ingestTasks;
    private final List<Task> analysisTasks;
    private final List<Task> visualsTasks;
    private final List<Task> audioTextTasks;
    private final List<Task> complianceTasks;
    private final List<Task> packagingTasks;

    public WorkflowOrchestrator() {
        this.ingestTasks = new ArrayList<>();
        this.analysisTasks = new ArrayList<>();
        this.visualsTasks = new ArrayList<>();
        this.audioTextTasks = new ArrayList<>();
        this.complianceTasks = new ArrayList<>();
        this.packagingTasks = new ArrayList<>();
    }

    public void addIngestTask(Task task) {
        this.ingestTasks.add(task);
    }

    public void addAnalysisTask(Task task) {
        this.analysisTasks.add(task);
    }

    public void addVisualsTask(Task task) {
        this.visualsTasks.add(task);
    }

    public void addAudioTextTask(Task task) {
        this.audioTextTasks.add(task);
    }

    public void addComplianceTask(Task task) {
        this.complianceTasks.add(task);
    }

    public void addPackagingTask(Task task) {
        this.packagingTasks.add(task);
    }

    public void runPipeline(MasterFile file) {
        if (!runPhase("Phase 1: Ingest", ingestTasks, file)) return;

        if (!runPhase("Phase 2: Analysis", analysisTasks, file)) return;

        if (!runPhase("Phase 3: Visuals", visualsTasks, file)) return;

        if (!runPhase("Phase 4: Audio/Text", audioTextTasks, file)) return;

        if (!runPhase("Phase 5: Compliance", complianceTasks, file)) return;

        if (!runPhase("Phase 6: Packaging", packagingTasks, file)) return;
    }

    private String getActionName(Task task) {
        switch(task.getName()) {
            case "Integrity Check": return "Validating Integrity";
            case "Format Validator": return "Validating Format";
            case "Intro/Outro Detector": return "Detecting Intro/Outro";
            case "Credit Roller": return "Scanning Credits";
            case "Scene Indexer": return "Indexing Scenes";
            case "Scene Complexity": return "Analyzing Complexity";
            case "Transcoder": return "Transcoding 4K/1080p/720p";
            case "Sprite Generator": return "Generating Sprites";
            case "Speech-to-Text": return "Extracting Audio & Transcribing";
            case "Translator": return "Translating Subtitles";
            case "AI Dubber": return "Synthesizing Dubs";
            case "Safety Scanner": return "Scanning for Safety";
            case "Regional Branding": return "Applying Regional Branding";
            case "DRM Wrapper": return "Encrypting Assets";
            case "Manifest Builder": return "Building Manifest";
            default: return "Executing " + task.getName();
        }
    }

    private boolean runPhase(String phaseName, List<Task> tasks, MasterFile file) {
        for (Task task : tasks) {
            System.out.print("[" + phaseName + "] " + getActionName(task) + "... ");
            ValidationResult result = task.execute(file);
            if (!result.isSuccess()) {
                System.out.println("FAILED (" + result.getMessage() + ")");
                return false;
            } else {
                if (task.getName().equals("Transcoder")) {
                    System.out.println("DONE");
                } else {
                    System.out.println("OK");
                }
            }
        }
        return true;
    }
}
