package ru.yandex.praktikum.login;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.*;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;

public class TestLoginWithInvalidCredentialsShouldFail {
    private UserClient userClient;
    private UserResponse userResponse;
    private ErrorResponse errorResponse;
    private String errorMessage;
    private int statusCode;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }
    @After
    public void clearDown() {
        userClient.deleteUser(userResponse);
    }

    @DisplayName("Логин с неверным паролем")
    @Description("Проверить, что пользователь не может залогиниться;\n" +
            "    - если указан некорректнный email и/или пароль;\n" +
            "    - запрос возвращает правильный код ответа;\n" +
            "    - успешный запрос возвращает ожидаемое тело ответа")
    @Test
    public void shouldNotLoginWithInvalidPassword() {
        User user = UserGenerator.getRandomData();
        ValidatableResponse createResponse = userClient.createNewUser(user);
        statusCode = createResponse.extract().statusCode();
        userResponse = createResponse.extract().as(UserResponse.class);
        assertEquals("The status code is invalid", HTTP_OK, statusCode);

        user.setPassword(UserGenerator.getRandomPassword().getPassword());
        ValidatableResponse loginResponse = userClient.login(UserLogin.from(user));
        statusCode = createResponse.extract().statusCode();

        assertEquals("The status code is invalid", HTTP_OK, statusCode);
        assertTrue(userResponse.isSuccess());
        errorResponse = loginResponse.extract().as(ErrorResponse.class);
        statusCode = loginResponse.extract().statusCode();
        errorMessage = loginResponse.extract().path("message");

        assertEquals("The status code is invalid", HTTP_UNAUTHORIZED, statusCode);
        assertFalse(errorResponse.isSuccess());
        assertEquals("email or password are incorrect", errorMessage);
    }
    @DisplayName("Логин с неверной электронной почтой")
    @Test
    public void shouldNotLoginWithInvalidEmail() {
        User user = UserGenerator.getRandomData();
        ValidatableResponse createResponse = userClient.createNewUser(user);
        statusCode = createResponse.extract().statusCode();
        userResponse = createResponse.extract().as(UserResponse.class);
        assertEquals("The status code is invalid", HTTP_OK, statusCode);

        user.setEmail(UserGenerator.getRandomEmail().getEmail());
        ValidatableResponse loginResponse = userClient.login(UserLogin.from(user));
        statusCode = createResponse.extract().statusCode();

        assertEquals("The status code is invalid", HTTP_OK, statusCode);
        assertTrue(userResponse.isSuccess());
        errorResponse = loginResponse.extract().as(ErrorResponse.class);
        statusCode = loginResponse.extract().statusCode();
        errorMessage = loginResponse.extract().path("message");

        assertEquals("The status code is invalid", HTTP_UNAUTHORIZED, statusCode);
        assertFalse(errorResponse.isSuccess());
        assertEquals("email or password are incorrect", errorMessage);
    }
    @DisplayName("Логин с неверной электроннной почтой и паролем")
    @Test
    public void shouldNotLoginWithInvalidEmailAndPassword() {
        User user = UserGenerator.getRandomData();
        ValidatableResponse createResponse = userClient.createNewUser(user);
        statusCode = createResponse.extract().statusCode();
        userResponse = createResponse.extract().as(UserResponse.class);
        assertEquals("The status code is invalid", HTTP_OK, statusCode);

        user.setEmail(UserGenerator.getRandomEmail().getEmail());
        user.setPassword(UserGenerator.getRandomPassword().getPassword());
        ValidatableResponse loginResponse = userClient.login(UserLogin.from(user));
        statusCode = createResponse.extract().statusCode();

        assertEquals("The status code is invalid", HTTP_OK, statusCode);
        assertTrue(userResponse.isSuccess());
        errorResponse = loginResponse.extract().as(ErrorResponse.class);
        statusCode = loginResponse.extract().statusCode();
        errorMessage = loginResponse.extract().path("message");

        assertEquals("The status code is invalid", HTTP_UNAUTHORIZED, statusCode);
        assertFalse(errorResponse.isSuccess());
        assertEquals("email or password are incorrect", errorMessage);
    }
}
