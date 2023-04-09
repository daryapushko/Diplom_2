package ru.yandex.praktikum.order.create;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.IngredientClient;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.*;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static org.junit.Assert.assertEquals;

public class TestInvalidOrderCreation {
    private OrderClient orderClient;
    private UserClient userClient;
    private IngredientClient ingredientClient;
    private Ingredients ingredients;
    private IngredientData ingredientData;
    private OrderRequest orderRequest;
    private ErrorResponse errorResponse;
    private String errorMessage;
    private int orderNumber;
    private int statusCode;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @DisplayName("Создание некорректного заказа и проверка кода ошибки")
    @Description("Проверить, что нельзя создать заказ;\n" +
            "    - c неверным хэшем ингредиентов;\n" +
            "    - сервер отвечает с кодом 500")
    @Test
    public void shouldNotCreateNewOrderWithoutValidIngredient() {
        //создать заказ
        orderRequest = OrderGenerator.getRandomData();
        //проверить ответ cервера
        ValidatableResponse orderResponse = orderClient.createNewOrder(orderRequest);
        statusCode = orderResponse.extract().statusCode();

        assertEquals("The status code is invalid", HTTP_INTERNAL_ERROR, statusCode);
    }

    @DisplayName("Создание пустого заказа и проверка тела ответа")
    @Description("Проверить, что нельзя создать заказ;\n" +
            "    - без ингредиентов;\n" +
            "    - тело ответа содержит искомое сообщение об ошибке")
    @Test
    public void shouldNotCreateNewOrderWithoutIngredients() {
        //создать заказ
        orderRequest = OrderGenerator.getZeroData();
        //проверить сообщение об ошибке
        ValidatableResponse orderResponse = orderClient.createNewOrder(orderRequest);
        statusCode = orderResponse.extract().statusCode();
        errorMessage = orderResponse.extract().as(ErrorResponse.class).getMessage();

        assertEquals("The status code is invalid", HTTP_BAD_REQUEST, statusCode);
        assertEquals("Ingredient ids must be provided", errorMessage);
    }
}
