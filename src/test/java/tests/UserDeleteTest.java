package tests;

import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Delete user cases")
@Feature("Ex18: Тесты на DELETE")
public class UserDeleteTest  extends BaseTestCase{
    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Description("Ex18.1 Negative. Тест проверяет невозможность удаления пользоваетля с ID = 2")
    @DisplayName("Тест  на попытку удалить пользователя по ID 2")
    @Test
    public void testDeleteOldUserWithoutAuth() {
        //Авторизируемся
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.
                makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/login/", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");

        //Удаляем пользователя с ID = 2
        Response responseDeleteUser = apiCoreRequests.
                makeDeleteRequest("https://playground.learnqa.ru/api_dev/user/2",
                authData,
                this.header,
                this.cookie
        );
        //Assertions.assertResponseTextEquals(responseDeleteUser, "{\"error\":\"Please, do not delete test users with ID 1, 2, 3, 4 or 5.\"}");
        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertJsonByName(responseDeleteUser, "error","Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }


    @Description("Ex18.2 Тест проверяет удаление авторизированного пользователя")
    @DisplayName("Тест проверяет удаление авторизированного пользователя")
    @Test
    public void testDeleteNewUserWithAuth() {
        //Создаем пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateUser = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", userData);

        String userId = responseCreateUser.jsonPath().get("id");

        //Авторизируемся
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.
                makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/login/", authData);

        String token = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");


        //Удаляем
        Response responseDeleteUser = apiCoreRequests.
                makeDeleteRequest("https://playground.learnqa.ru/api_dev/user/" + userId,
                        authData,
                        token,
                        cookie
                );


        //Пробуем получить данные удаленного пользователя
        Response responseUserData = apiCoreRequests.
                makeGetRequest("https://playground.learnqa.ru/api_dev/user/" + userId,
                        token,
                        cookie);

        Assertions.assertJsonByName(responseDeleteUser, "success", "!");
        Assertions.assertResponseTextEquals(responseUserData, "User not found");

    }


    @Description("Ex18.3 Negative. Тест проверяет удаление пользователя, будучи авторизованными под другим пользователем")
    @DisplayName("Тест проверяет удаление другого пользователя")
    @Test
    public void testDeleteNewUserWithAnotherAuth() {
        //Создаем пользователя 1
        Map<String, String> user1Data = DataGenerator.getRegistrationData();

        Response responseCreateUser1 = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", user1Data);

        String user1Id = responseCreateUser1.jsonPath().get("id");

        //Создаем пользователя 2
        Map<String, String> user2Data = DataGenerator.getRegistrationData();

        Response responseCreateUser2 = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", user2Data);

        //Авторизируемся под пользователем 2
        Map<String, String> authData = new HashMap<>();
        authData.put("email", user2Data.get("email"));
        authData.put("password", user2Data.get("password"));

        Response responseGetAuthUser2 = apiCoreRequests.
                makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/login/", authData);

        String cookie = this.getCookie(responseGetAuthUser2, "auth_sid");
        String token = this.getHeader(responseGetAuthUser2, "x-csrf-token");

        //Удаляем
        Response responseDeleteUser = apiCoreRequests.
                makeDeleteRequestForDeleteAnotherUser("https://playground.learnqa.ru/api_dev/user/" + user1Id,
                        token,
                        cookie
                );

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertJsonByName(responseDeleteUser,"error", "This user can only delete their own account.");
    }
}
