package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Epic("Edit user cases")
@Feature("Edit")
public class UserEditTest extends BaseTestCase {
    String cookie;
    String header;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    /*
     * Создаем нового пользователя с id>10 для последующего редактирования
     * */
    @Description("Тест проверяет изменение имени у авторизированного пользователя")
    @DisplayName("Тест проверяет изменение имени у авторизированного пользователя")
    @Test
    public void testEditJustCreatedTest() {
        //GENERATE USER
        Map<String , String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.
                makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", userData).jsonPath();
        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.
                makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/login/", authData);

        String header = responseGetAuth.getHeader("x-csrf-token");
        String token = responseGetAuth.getCookie("auth_sid");

        //EDIT
        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        /*
         * В PUT-запрос передаем авторизационные куки и заголовок
         * и через боди передаем параметры, которые хотим изменить
         * и его новое значение (в переменной newName)
         * */
        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api_dev/user/" + userId,
                editData,
                header,
                token
        );

        //GET
        /*
         * Авторизованный GET- запрос на получение данных
         * Убеждаемся, что в ответе firstName равен новому имени
         * */
        Response responseUserData = apiCoreRequests.
                makeGetRequest("https://playground.learnqa.ru/api_dev/user/" + userId,
                        header,
                        token);

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Description("Ex17.1 Negative. Тест проверяет попытку изменения данных пользователя, будучи неавторизованными")
    @DisplayName("Тест проверяет попытку изменения данных пользователя, будучи неавторизованными")
    @Owner(value = "DLZolottsev")
    @Test
    public void testEditUnauthorizedUser() {
        //Создаем пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateUser = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", userData);
        String userId = responseCreateUser.jsonPath().get("id");

        //Изменяем имя
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makeNoAuthPutRequest("https://playground.learnqa.ru/api_dev/user/" + userId,
                editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error","Auth token not supplied");
    }

    @Description("Ex17.2 Negative. Тест проверяет попытку изменения данных пользователя, будучи авторизованными другим пользователем")
    @DisplayName("Тест проверяет изменение имени будучи авторизованными другим пользователем")
    @Owner(value = "DLZolottsev")
    @Test
    public void testEditAnotherUser() {
        //Создаем пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.
                makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");


        //Авторизируемся под другим пользователем
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("vinkotov@example.com"));
        authData.put("password", userData.get("1234"));

        Response responseGetAuth = apiCoreRequests.
                makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/login/", authData);

        String header = responseGetAuth.getHeader("x-csrf-token");
        String token = responseGetAuth.getCookie("auth_sid");


        //Изменяем имя
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.
                makePutRequest("https://playground.learnqa.ru/api_dev/user/" + userId,
                        editData,
                        header,
                        token);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error","Auth token not supplied");
    }

    @Description("Ex17.3 Negative. Тест проверяет попытку изменения email на невалидный, будучи авторизованными тем же пользователем")
    @DisplayName("Тест проверяет изменение email на невалидный")
    @Owner(value = "DLZolottsev")
    @Test
    public void testEditInvalidEmail() {
        //Создаем пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.
                makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");

        //Авторизируемся
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.
                makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/login/", authData);

        String header = responseGetAuth.getHeader("x-csrf-token");
        String token = responseGetAuth.getCookie("auth_sid");

        //Изменяем имя
        String newEmail = "amail.ru";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api_dev/user/" + userId,
                editData,
                header,
                token
        );

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error","Invalid email format");
    }


    @Description("Ex17.4 Negative. Тест проверяет попытку изменения firstName пользователя, будучи авторизованными тем же пользователем, на очень короткое значение в один символ")
    @DisplayName("Тест проверяет изменение имени на невалидное")
    @Owner(value = "DLZolottsev")
    @Test
    public void testEditUserInvalidFirstName() {
        //Создаем пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();

        ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
        JsonPath responseCreateAuth = apiCoreRequests.
                makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");

        //Авторизируемся
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.
                makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/login/", authData);

        String header = responseGetAuth.getHeader("x-csrf-token");
        String token = responseGetAuth.getCookie("auth_sid");


        //Изменяем имя
        String newName = "q";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api_dev/user/" + userId,
                editData,
                header,
                token
        );

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error","The value for field `firstName` is too short");
    }

}