package ru.yandex.praktikum.model;

public class Order {
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Order{" +
                "number=" + number +
                '}';
    }
}
