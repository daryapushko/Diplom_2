package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.client.base.ClientConfiguration;
import ru.yandex.praktikum.model.OrderRequest;
import ru.yandex.praktikum.model.UserResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends ClientConfiguration {
    private static final String ORDERS_URI = URI + "/api/orders/";

    @Step("Get all orders")
    public ValidatableResponse getOrderList() {
        return given()
                .spec(getHeader())
                .when()
                .get(ORDERS_URI + "all")
                .then();
    }

    @Step("Create a new order {order}")
    public ValidatableResponse createNewOrder(OrderRequest orderRequest) {
        return given()
                .spec(getHeader())
                .body(orderRequest)
                .when()
                .post(ORDERS_URI)
                .then();
    }

    @Step("Create a new order {order} with authorised user {user}")
    public ValidatableResponse createNewAuthorisedOrder(OrderRequest orderRequest, UserResponse userResponse) {
        return given()
                .spec(getHeaderwithAuthorisation(userResponse.getAccessToken()))
                .body(orderRequest)
                .when()
                .post(ORDERS_URI)
                .then();
    }

    @Step("Get orders for a specific authorised user {user}")
    public ValidatableResponse getOrderListWithAuthorisedUser(UserResponse userResponse) {
        return given()
                .spec(getHeaderwithAuthorisation(userResponse.getAccessToken()))
                .when()
                .get(ORDERS_URI)
                .then();
    }

    @Step("Get orders for a non-authorised user")
    public ValidatableResponse requestOrderListWithNonAuthorisedUser() {
        return given()
                .spec(getHeader())
                .when()
                .get(ORDERS_URI)
                .then();
    }
}
