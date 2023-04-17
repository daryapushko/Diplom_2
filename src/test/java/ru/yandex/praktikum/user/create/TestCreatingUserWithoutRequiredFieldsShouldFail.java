package ru.yandex.praktikum.user.create;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.UserClient;
import ru.yandex.praktikum.model.ErrorResponse;
import ru.yandex.praktikum.model.User;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class TestCreatingUserWithoutRequiredFieldsShouldFail {
    private final String email;
    private final String password;
    private final String name;
    private UserClient userClient;
    private ErrorResponse errorResponse;
    private String errorMessage;
    private int statusCode;

    public TestCreatingUserWithoutRequiredFieldsShouldFail(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters(name = "Email, password и name. Некорректные тестовые данные: {0} {1} {2}.")
    public static Object[][] getIncorrectUserDataSet() {
        return new Object[][]{
                {RandomStringUtils.randomAlphanumeric(3) + "@yandex.ru", "", ""},
                {RandomStringUtils.randomAlphanumeric(3) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(5), ""},
                {"", RandomStringUtils.randomAlphanumeric(5), ""},
                {"", "", RandomStringUtils.randomAlphabetic(5)},
                {RandomStringUtils.randomAlphanumeric(3) + "@yandex.ru", "", RandomStringUtils.randomAlphanumeric(5)},
                {"", RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphabetic(5)},
                {"", "", ""}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @DisplayName("Нельзя создать пользователя и не заполнить одно из обязательных полей")
    @Description("Проверить, что пользователя без имени нельзя создать;\n" +
            "    - запрос возвращает правильный код ответа;\n" +
            "    - запрос возвращает ожидаемое тело ответа")

    @Test
    public void shouldNotCreateNewUserWithoutRequiredField() {
        User user = new User();
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setName(this.name);
        ValidatableResponse createResponse = userClient.createNewUser(user);
        errorResponse = createResponse.extract().as(ErrorResponse.class);
        statusCode = createResponse.extract().statusCode();
        errorMessage = createResponse.extract().path("message");

        assertEquals("The status code is invalid", HTTP_FORBIDDEN, statusCode);
        assertFalse(errorResponse.isSuccess());
        assertEquals("Email, password and name are required fields", errorMessage);
    }

}
