package com.zela.app.step;

import java.util.List;

public class StepResult {
    private String stepName;
    private List<String> error;
    private String status;

    public StepResult(String stepName, List<String> error, String status) {
        this.stepName = stepName;
        this.error = error;
        this.status = status;
    }
}
