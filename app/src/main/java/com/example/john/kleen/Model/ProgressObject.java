package com.example.john.kleen.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class ProgressObject implements Serializable {

    private int steps;
    private int step_goal;
    private LocalDate date;
    private int weight;
    private double calories;

    public ProgressObject(int steps, int step_goal, LocalDate date, int weight, double calories) {
        this.steps = steps;
        this.step_goal = step_goal;
        this.date = date;
        this.weight = weight;
        this.calories = calories;
    }

    public int getSteps() {
        return steps;
    }

    public int getStep_goal() {
        return step_goal;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getWeight() {
        return weight;
    }

    public double getCalories() {
        return calories;
    }
}
