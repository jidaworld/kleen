package com.example.john.kleen.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProgressObject  {

    private int steps;
    private int step_goal;
    private String date;
    private int weight;

    public ProgressObject(int steps, int step_goal, int weight) {
        this.steps = steps;
        this.step_goal = step_goal;
        this.weight = weight;
        Date currentDate = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("d, M, yyyy");
        date = simpleDate.format(currentDate);
    }

    public ProgressObject(int steps) {
        this.steps = steps;
        Date currentDate = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("d, M, yyyy");
        date = simpleDate.format(currentDate);
    }

    public ProgressObject(){

    }


    public int getSteps() {
        return steps;
    }

    public int getStep_goal() {
        return step_goal;
    }

    public String getDate() {
        return date;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "ProgressObject{" +
                "steps=" + steps +
                ", step_goal=" + step_goal +
                ", date='" + date + '\'' +
                ", weight=" + weight +
                '}';
    }
}
