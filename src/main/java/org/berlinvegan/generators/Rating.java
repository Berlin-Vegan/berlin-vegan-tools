package org.berlinvegan.generators;

public class Rating {
    float value;
    int number;

    public Rating(float value, int number) {
        this.value = value;
        this.number = number;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
