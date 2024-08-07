package tests;

import io.qameta.allure.Description;

import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
* Если мы просматриваем данные пользователя, то есть присылаем авторизованный запрос и сервер понимает,
* что мы авторизованы из-под того же пользователя, чьи данные запрашиваем, мы должны видеть все поля: имя, фамилию, email.
* Иначе, если запрашиваем данные чужого пользователя, мы должны видеть только логин
*/
public class UserGetTest extends BaseTestCase {
    String cookie;
    String header;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api_dev/user/2")
                .andReturn();
        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstname");
        Assertions.assertJsonHasNotField(responseUserData, "lastname");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test
    public void testGetUserDetailsAuthAsSameUser(){
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api_dev/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api_dev/user/2")
                .andReturn();

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);

    }

    @Test
    @Description("Ex16 Тест проверяет, что получаем только username, когда авторизовываемся под одним пользователем, но получаем данные другого.")
    @DisplayName("Test returns only username for another user")
    @Owner(value = "DLZolottsev")
    public void testGetUserDetailsAuthAsAnotherUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");

        //Получаем данные другого пользователя
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api_dev/user/1",
                        this.header,
                        this.cookie
                );

        String[] expectedFields = {"username"};
        String[] unExpectedFields = {"password", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
        Assertions.assertJsonHasNotField(responseUserData, Arrays.toString(unExpectedFields));
    }
}
