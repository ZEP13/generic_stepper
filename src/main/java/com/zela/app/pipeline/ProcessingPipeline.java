package com.zela.app.pipeline;

import java.util.ArrayList;
import java.util.List;
import com.zela.app.Step;
import com.zela.app.step.StepReport;
import com.zela.app.step.StepResult;

public class ProcessingPipeline<T> implements Step<T> {
    private final List<Step<T>> steps;
    private final StepResult stepResult;
    private final StepReport stepReport;

    public ProcessingPipeline() {
        this.steps = new ArrayList<>();
        this.stepResult = new StepResult("ProcessingPipeline", new ArrayList<>(), "INIT");
        this.stepReport = new StepReport();
    }

    public ProcessingPipeline(List<Step<T>> steps, StepResult stepResult, StepReport stepReport) {
        this.steps = steps;
        this.stepResult = stepResult;
        this.stepReport = stepReport;
    }

    public ProcessingPipeline<T> addStep(Step<T> step) {
        this.steps.add(step);
        return this;
    }

    @Override
    public List<T> execute(List<T> input) {
        List<T> currentData = input;
        List<String> errors = stepReport.getError();
        try {
            for (Step<T> step : steps) {
                currentData = step.execute(currentData);
            }
            stepReport.setStatus("Traitement des données terminé avec succès.");
        } catch (Exception e) {
            errors.add(e.getMessage());
            stepReport.setStatus("Il y a " + errors.size() + " erreur(s) dans le traitement des données.");
        }
        return currentData;
    }
}
