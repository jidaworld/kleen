package com.example.john.kleen.Model;

public class WeightCalories {

    private int weight;
    private int calories;
    private int goal;

    public WeightCalories(int weight, int calories, int goal) {
        this.weight = weight;
        this.calories = calories;
        this.goal = goal;
    }

    public int getGoal() {
        return goal;
    }

    public int getWeight() {
        return weight;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public String toString() {
        return "WeightCalories{" +
                "weight=" + weight +
                ", calories=" + calories +
                ", goal=" + goal +
                '}';
    }
}
