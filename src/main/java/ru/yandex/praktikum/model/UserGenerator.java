package ru.yandex.praktikum.model;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Locale;

public class UserGenerator {
    public static User getRandomData() {
        String email = RandomStringUtils.randomAlphanumeric(10).toLowerCase(Locale.ROOT) + "@mail.ru";
        String password = RandomStringUtils.randomAlphanumeric(10);
        String name = RandomStringUtils.randomAlphanumeric(10);
        return new User(email, password, name);
    }

    public static User getRandomEmail() {
        String email = RandomStringUtils.randomAlphanumeric(5).toLowerCase(Locale.ROOT) + "@mail.ru";
        User user = new User();
        user.setEmail(email);
        return user;
    }

    public static User getRandomPassword() {
        String password = RandomStringUtils.randomAlphanumeric(5);
        User user = new User();
        user.setPassword(password);
        return user;
    }

    public static User getRandomName() {
        String name = RandomStringUtils.randomAlphanumeric(5);
        User user = new User();
        user.setPassword(name);
        return user;
    }
}
