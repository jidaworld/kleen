package com.example.john.kleen.Model;

public class StepEvent {

    private static int steps;

    public StepEvent(int steps) {
        this.steps = steps;
    }

    public static int getSteps() {
        return steps;
    }
}
