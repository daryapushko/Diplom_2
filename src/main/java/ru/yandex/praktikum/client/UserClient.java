package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.client.base.ClientConfiguration;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.UserLogin;
import ru.yandex.praktikum.model.UserResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends ClientConfiguration {
    private static final String USER_URI = URI + "/api/auth/";

    @Step("Register a new user {user}")
    public ValidatableResponse createNewUser(User user) {
        return given()
                .spec(getHeader())
                .body(user)
                .when()
                .post(USER_URI + "register")
                .then();
    }

    @Step("Login as user {userLogin}")
    public ValidatableResponse login(UserLogin userLogin) {
        return given()
                .spec(getHeader())
                .body(userLogin)
                .when()
                .post(USER_URI + "login")
                .then();
    }

    @Step("Update the created user {user}")
    public ValidatableResponse updateUser(User user, UserResponse userResponse) {
        return given()
                .spec(getHeaderwithAuthorisation(userResponse.getAccessToken()))
                .body(user)
                .when()
                .patch(USER_URI + "user")
                .then();
    }

    @Step("Non-authorised updating the created user {user}")
    public ValidatableResponse unauthRequestUpdateUser(User user) {
        return given()
                .spec(getHeader())
                .body(user)
                .when()
                .patch(USER_URI + "user")
                .then();
    }

    @Step("Delete user {userResponse}")
    public ValidatableResponse deleteUser(UserResponse userResponse) {
        return given()
                .spec(getHeaderwithAuthorisation(userResponse.getAccessToken()))
                .when()
                .delete(USER_URI + "user")
                .then();
    }
}