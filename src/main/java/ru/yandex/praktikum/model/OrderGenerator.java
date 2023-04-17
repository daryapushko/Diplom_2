package ru.yandex.praktikum.model;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Locale;

public class OrderGenerator {
    public static OrderRequest getRandomData() {
        String[] invalidIngredient = {RandomStringUtils.randomAlphanumeric(10).toLowerCase(Locale.ROOT)};
        OrderRequest orderRequest = new OrderRequest(invalidIngredient);
        orderRequest.setIngredients(invalidIngredient);
        return new OrderRequest(invalidIngredient);
    }

    public static OrderRequest getZeroData() {
        String[] invalidIngredient = {};
        OrderRequest orderRequest = new OrderRequest(invalidIngredient);
        orderRequest.setIngredients(invalidIngredient);
        return new OrderRequest(invalidIngredient);
    }
}
