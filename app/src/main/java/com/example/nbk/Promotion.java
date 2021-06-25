package com.example.nbk;

public class Promotion {
    public String name;
    public String percent;
    public String category;

    public Promotion(String name, String percent, String category){
        this.name = name;
        this.percent = percent;
        this.category = category;
    }

    @Override
    public String toString() {
        return "An offer you can’t miss. Get " +
                this.percent + "% discount when at " +
                this.name + "when using your NBK Visa Cards.";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
