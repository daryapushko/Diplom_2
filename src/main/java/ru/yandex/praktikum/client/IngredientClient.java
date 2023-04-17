package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.client.base.ClientConfiguration;

import static io.restassured.RestAssured.given;

public class IngredientClient extends ClientConfiguration {
    private static final String INGREDIENTS_URI = URI + "/api/ingredients";

    @Step("Get ingredients")
    public ValidatableResponse getIngredientList() {
        return given()
                .spec(getHeader())
                .when()
                .get(INGREDIENTS_URI)
                .then();
    }
}
