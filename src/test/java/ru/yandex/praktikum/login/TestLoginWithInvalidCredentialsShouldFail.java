package ru.yandex.praktikum.login;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.*;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.junit.Assert.*;

public class TestLoginWithInvalidCredentialsShouldFail {
    private UserClient userClient;
    private UserResponse userResponse;
    private ErrorResponse errorResponse;
    private String errorMessage;
    private int statusCodeCreate;
    private int statusCodeLogin;
    private int statusCodeLoginResponse;


    @Before
    public void setUp() {
        userClient = new UserClient();
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

    @DisplayName("Логин с неверным паролем")
    @Description("Проверить, что пользователь не может залогиниться;\n" +
            "    - если указан некорректнный email и/или пароль;\n" +
            "    - запрос возвращает правильный код ответа;\n" +
            "    - успешный запрос возвращает ожидаемое тело ответа")
    @Test
    public void shouldNotLoginWithInvalidPassword() {
        User user = UserGenerator.getRandomData();
        ValidatableResponse createResponse = userClient.createNewUser(user);
        statusCodeCreate = createResponse.extract().statusCode();
        UserResponse userResponse = createResponse.extract().as(UserResponse.class);

        user.setPassword(UserGenerator.getRandomPassword().getPassword());
        ValidatableResponse loginResponse = userClient.login(UserLogin.from(user));
        statusCodeLogin = createResponse.extract().statusCode();

        errorResponse = loginResponse.extract().as(ErrorResponse.class);
        statusCodeLoginResponse = loginResponse.extract().statusCode();
        errorMessage = loginResponse.extract().path("message");

        assertEquals("The status code for registration is invalid", HTTP_OK, statusCodeCreate);
        assertEquals("The status code for login is invalid", HTTP_OK, statusCodeLogin);
        assertTrue(userResponse.isSuccess());
        assertEquals("The status code for login response is invalid", HTTP_UNAUTHORIZED, statusCodeLoginResponse);
        assertFalse(errorResponse.isSuccess());
        assertEquals("email or password are incorrect", errorMessage);
    }

    @DisplayName("Логин с неверной электронной почтой")
    @Test
    public void shouldNotLoginWithInvalidEmail() {
        User user = UserGenerator.getRandomData();
        ValidatableResponse createResponse = userClient.createNewUser(user);
        statusCodeCreate = createResponse.extract().statusCode();
        userResponse = createResponse.extract().as(UserResponse.class);

        user.setEmail(UserGenerator.getRandomEmail().getEmail());
        ValidatableResponse loginResponse = userClient.login(UserLogin.from(user));
        int statusCodeLogin = createResponse.extract().statusCode();

        errorResponse = loginResponse.extract().as(ErrorResponse.class);
        int statusCodeLoginResponse = loginResponse.extract().statusCode();
        errorMessage = loginResponse.extract().path("message");

        assertEquals("The status code for registration is invalid", HTTP_OK, statusCodeCreate);
        assertEquals("The status code for login is invalid", HTTP_OK, statusCodeLogin);
        assertTrue(userResponse.isSuccess());
        assertEquals("The status code for login response is invalid", HTTP_UNAUTHORIZED, statusCodeLoginResponse);
        assertFalse(errorResponse.isSuccess());
        assertEquals("email or password are incorrect", errorMessage);
    }

    @DisplayName("Логин с неверной электроннной почтой и паролем")
    @Test
    public void shouldNotLoginWithInvalidEmailAndPassword() {
        User user = UserGenerator.getRandomData();
        ValidatableResponse createResponse = userClient.createNewUser(user);
        statusCodeCreate = createResponse.extract().statusCode();
        userResponse = createResponse.extract().as(UserResponse.class);

        user.setEmail(UserGenerator.getRandomEmail().getEmail());
        user.setPassword(UserGenerator.getRandomPassword().getPassword());
        ValidatableResponse loginResponse = userClient.login(UserLogin.from(user));
        statusCodeLogin = createResponse.extract().statusCode();

        errorResponse = loginResponse.extract().as(ErrorResponse.class);
        statusCodeLoginResponse = loginResponse.extract().statusCode();
        errorMessage = loginResponse.extract().path("message");

        assertEquals("The status code for registration is invalid", HTTP_OK, statusCodeCreate);
        assertEquals("The status code for login is invalid", HTTP_OK, statusCodeLogin);
        assertTrue(userResponse.isSuccess());
        assertEquals("The status code for login response is invalid", HTTP_UNAUTHORIZED, statusCodeLoginResponse);
        assertFalse(errorResponse.isSuccess());
        assertEquals("email or password are incorrect", errorMessage);
    }
}
