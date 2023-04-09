package ru.yandex.praktikum.client.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ClientConfiguration {
    protected static final String URI = "https://stellarburgers.nomoreparties.site";

    protected RequestSpecification getHeader() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(URI)
                .build();
    }

    protected RequestSpecification getHeaderwithAuthorisation(String accessToken) {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(URI)
                .addHeader("Authorization", accessToken)
                .build();
    }
}