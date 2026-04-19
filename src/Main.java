import com.streaming.pipeline.core.WorkflowOrchestrator;
import com.streaming.pipeline.models.MasterFile;
import com.streaming.pipeline.phases.ingest.FormatValidatorTask;
import com.streaming.pipeline.phases.ingest.IntegrityCheckTask;
import com.streaming.pipeline.phases.analysis.CreditRollerTask;
import com.streaming.pipeline.phases.analysis.IntroOutroDetectorTask;
import com.streaming.pipeline.phases.analysis.SceneIndexerTask;
import com.streaming.pipeline.phases.visuals.SceneComplexityTask;
import com.streaming.pipeline.phases.visuals.SpriteGeneratorTask;
import com.streaming.pipeline.phases.visuals.TranscoderTask;
import com.streaming.pipeline.phases.audiotext.AIDubberTask;
import com.streaming.pipeline.phases.audiotext.SpeechToTextTask;
import com.streaming.pipeline.phases.audiotext.TranslatorTask;
import com.streaming.pipeline.phases.compliance.RegionalBrandingTask;
import com.streaming.pipeline.phases.compliance.SafetyScannerTask;
import com.streaming.pipeline.phases.packaging.DRMWrapperTask;
import com.streaming.pipeline.phases.packaging.ManifestBuilderTask;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        WorkflowOrchestrator orchestrator = new WorkflowOrchestrator();
        
        orchestrator.addIngestTask(new IntegrityCheckTask());
        orchestrator.addIngestTask(new FormatValidatorTask());
        
        orchestrator.addAnalysisTask(new IntroOutroDetectorTask());
        orchestrator.addAnalysisTask(new CreditRollerTask());
        orchestrator.addAnalysisTask(new SceneIndexerTask());
        
        orchestrator.addVisualsTask(new SceneComplexityTask());
        orchestrator.addVisualsTask(new TranscoderTask());
        orchestrator.addVisualsTask(new SpriteGeneratorTask());
        
        orchestrator.addAudioTextTask(new SpeechToTextTask());
        orchestrator.addAudioTextTask(new TranslatorTask());
        orchestrator.addAudioTextTask(new AIDubberTask());
        
        orchestrator.addComplianceTask(new SafetyScannerTask());
        orchestrator.addComplianceTask(new RegionalBrandingTask());
        
        orchestrator.addPackagingTask(new DRMWrapperTask());
        orchestrator.addPackagingTask(new ManifestBuilderTask());
        
        String videoPath = "master.mp4";
        File downloadedVideo = new File(videoPath);
        
        if (!downloadedVideo.exists()) {
            System.out.println("ERROR: Could not find the real downloaded video at " + videoPath);
            return;
        }

        long actualSizeInBytes = downloadedVideo.length();
        
        String extension = "";
        int i = videoPath.lastIndexOf('.');
        if (i > 0) {
            extension = videoPath.substring(i+1).toUpperCase();
        }
        
        MasterFile uploadedFile = new MasterFile(
                downloadedVideo.getName(), 
                actualSizeInBytes, 
                extension
        );
        
        orchestrator.runPipeline(uploadedFile);
    }
}
