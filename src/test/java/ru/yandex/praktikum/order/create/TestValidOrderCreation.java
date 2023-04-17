package ru.yandex.praktikum.order.create;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.IngredientClient;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.*;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestValidOrderCreation {
    private OrderClient orderClient;
    private UserClient userClient;
    private IngredientClient ingredientClient;
    private Ingredients ingredients;
    private OrderRequest orderRequest;
    private UserResponse userResponse;
    private int orderNumber;
    private int statusCode;

    @Before
    public void setUp() {
        ingredientClient = new IngredientClient();
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @After
    public void clearDown() {
        try {
            userClient.deleteUser(userResponse);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            System.out.println("No user found to delete");
        }
    }

    @DisplayName("Создание заказа и проверка тела ответа")
    @Description("Проверить, что можно создать заказ;\n" +
            "    - с авторизацией;\n" +
            "    - необходимо указывать хэш ингредиента\n" +
            "    - тело ответа содержит номер заказа")
    @Test
    public void shouldCreateNewOrderWithAuthorisedUser() {
        //cоздать пользователя и использовать его токен для авторизации при заказе
        User user = UserGenerator.getRandomData();
        ValidatableResponse createResponse = userClient.createNewUser(user);
        statusCode = createResponse.extract().statusCode();
        userResponse = createResponse.extract().as(UserResponse.class);
        //получить ингредиент для заказа
        ValidatableResponse ingredientsResponse = ingredientClient.getIngredientList();
        statusCode = ingredientsResponse.extract().statusCode();
        ingredients = ingredientsResponse.extract().body().as(Ingredients.class);
        String ingredientId = ingredients.getData().get(0).get_id();
        String[] ingredientList = new String[]{ingredientId};
        //создать заказ
        orderRequest = new OrderRequest(ingredientList);
        //проверить ответ
        ValidatableResponse orderResponse = orderClient.createNewAuthorisedOrder(orderRequest, userResponse);
        statusCode = orderResponse.extract().statusCode();
        orderNumber = orderResponse.extract().as(OrderResponse.class).getOrder().getNumber();

        assertEquals("The status code is invalid", HTTP_OK, statusCode);
        assertTrue("The order is not created", orderNumber != 0);
    }

    @DisplayName("Создание заказа и проверка тела ответа")
    @Description("Проверить, что можно создать заказ;\n" +
            "    - без авторизации;\n" +
            "    - необходимо указывать хэш ингредиента\n" +
            "    - тело ответа содержит номер заказа")
    @Test
    public void shouldCreateNewOrderWithNonAuthorisedUser() {
        //получить ингредиент для заказа
        ValidatableResponse ingredientsResponse = ingredientClient.getIngredientList();
        statusCode = ingredientsResponse.extract().statusCode();
        ingredients = ingredientsResponse.extract().body().as(Ingredients.class);
        String ingredientId = ingredients.getData().get(0).get_id();
        String[] ingredientList = new String[]{ingredientId};
        //создать заказ
        orderRequest = new OrderRequest(ingredientList);
        //проверить ответ
        ValidatableResponse orderResponse = orderClient.createNewOrder(orderRequest);
        statusCode = orderResponse.extract().statusCode();
        orderNumber = orderResponse.extract().as(OrderResponse.class).getOrder().getNumber();

        assertEquals("The status code is invalid", HTTP_OK, statusCode);
        assertTrue("The order is not created", orderNumber != 0);
    }
}
