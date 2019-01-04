package com.example.john.kleen.Model;

public class WeightCalories {

    private int weight = 0;
    private int calories = 0;
    private int goal = 0;

    public WeightCalories(int weight, int calories, int goal) {
        this.weight = weight;
        this.calories = calories;
        this.goal = goal;
    }

    public WeightCalories() {
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setCalories(int calories) {
        this.calories = calories;
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
