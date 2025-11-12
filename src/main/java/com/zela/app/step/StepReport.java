package com.zela.app.step;

import java.util.List;

public class StepReport {

    private String stepName;
    private List<String> error;
    private String status;

    public StepReport() {
    }

    public StepReport(String stepName, List<String> error, String status) {
        this.stepName = stepName;
        this.error = error;
        this.status = status;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
