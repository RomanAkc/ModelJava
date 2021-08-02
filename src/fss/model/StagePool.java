package fss.model;

import java.util.ArrayList;

public class StagePool {
    private boolean alreadyCalculated = false;
    private ArrayList<Stage> stages = new ArrayList<>();

    public StagePool() {
    }

    public StagePool(ArrayList<Stage> stages) {
        this.stages = stages;
    }

    public void addStage(Stage s) {
        stages.add(s);
    }

    public void calc() {
        if(alreadyCalculated) {
            return;
        }

        for(var s : stages) {
            s.calc();
        }

        alreadyCalculated = true;
    }
}
