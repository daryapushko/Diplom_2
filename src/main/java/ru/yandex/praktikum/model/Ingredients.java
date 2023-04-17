package ru.yandex.praktikum.model;

import java.util.ArrayList;

public class Ingredients {
    private boolean success;
    private ArrayList<IngredientData> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<IngredientData> getData() {
        return data;
    }

    public void setData(ArrayList<IngredientData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "success=" + success +
                ", data=" + data +
                '}';
    }
}
