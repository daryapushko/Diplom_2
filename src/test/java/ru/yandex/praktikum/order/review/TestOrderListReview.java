package ru.yandex.praktikum.order.review;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.*;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.junit.Assert.*;

public class TestOrderListReview {
    private UserClient userClient;
    private OrderClient orderClient;
    private UserResponse userResponse;
    private int statusCode;
    private ErrorResponse errorResponse;
    private OrderListResponse orderListResponse;

    @Before
    public void setUp() {
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

    @DisplayName("Получение заказов конкретного пользователя")
    @Description("Проверить, что можно получить список заказов;\n" +
            "    - с авторизацией;\n" +
            "    - тело ответа содержит список заказов")
    @Test
    public void shouldReviewListForAuthorisedUserAndCheckResponse() {
        //создать пользователя и получить токен
        User user = UserGenerator.getRandomData();
        ValidatableResponse createResponse = userClient.createNewUser(user);
        statusCode = createResponse.extract().statusCode();
        userResponse = createResponse.extract().as(UserResponse.class);
        //сделать запрос и проверить тело ответа
        ValidatableResponse reviewResponse = orderClient.getOrderListWithAuthorisedUser(userResponse);
        statusCode = reviewResponse.extract().statusCode();
        orderListResponse = reviewResponse.extract().body().as(OrderListResponse.class);

        assertEquals("The status code is invalid", HTTP_OK, statusCode);
        assertTrue(orderListResponse.isSuccess());
    }

    @DisplayName("Нельзя получить заказы неавторизованного пользователя")
    @Description("Проверить, что запрос не возвращает список заказов;\n" +
            "    - без авторизации пользователя (нет токена);\n" +
            "    - приходит правильный код ответа;\n" +
            "    - тело ответа содержит сообщение об ошибке ")
    @Test
    public void shouldNotReviewOrderListForNonAuthorisedUserAndCheckResponse() {
        ValidatableResponse reviewOrdersResponse = orderClient.requestOrderListWithNonAuthorisedUser();
        statusCode = reviewOrdersResponse.extract().statusCode();
        errorResponse = reviewOrdersResponse.extract().as(ErrorResponse.class);

        assertEquals("The status code is invalid", HTTP_UNAUTHORIZED, statusCode);
        assertFalse(errorResponse.isSuccess());
        assertEquals("You should be authorised", errorResponse.getMessage());
    }
}