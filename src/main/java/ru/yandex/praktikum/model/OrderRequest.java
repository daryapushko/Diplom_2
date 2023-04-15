package ru.yandex.praktikum.model;

public class OrderRequest {
    private String[] ingredients;

    public OrderRequest(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "ingredients=" + ingredients +
                '}';
    }
}
