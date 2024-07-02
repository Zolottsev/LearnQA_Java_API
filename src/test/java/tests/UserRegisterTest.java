package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static lib.DataGenerator.getRegistrationData;

@Epic("User Register cases")
@Feature("User register")
public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Description("Тест проверяет невозможность создания пользователя с существующим email")
    @DisplayName("Тест проверяет невозможность создания пользователя с существующим email")
    @Test
    public void testCreateUserWithExistingEmail() {

        String email = "vinkotov@examle.com";
        /*
        * Данные берем из генератора и наш существующий email
        * */
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '"+ email + "' already exists");
    }

    @Description("Тест успешного создания пользователя")
    @DisplayName("Тест успешного создания пользователя")
    @Test
    public void testCreateUserSuccessfully() {

        String email = DataGenerator.getRandomEmail();

        /*
        * Сюда ничего не передаем, все данные дефолтные из генератора
        * */
        Map<String, String> userData = getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Description("Ex15 Тест проверяет невозможность создания пользователя с некорректным email")
    @DisplayName("Тест проверяет невозможность создания пользователя с некорректным email")
    @Test
    public void testCreateUserWithIncorrectEmail() {
        String email = "vinkotovexample.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = getRegistrationData(userData);
        Response responseCreateAuth = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @Description("Ex15 Тест проверяет невозможность создания пользователя  без указания одного из полей")
    @DisplayName("Тест проверяет невозможность создания пользователя  без указания одного из полей")
    @ParameterizedTest
    @CsvSource({
            "email",
            "password",
            "firstName",
            "lastName",
            "username"
    })

    public void testCreateUserWithOneIncorrectField(String emptyValue) {
        Map<String, String> nonDefaultValues = new HashMap<>();
        nonDefaultValues.put(emptyValue, "");

        Map<String, String> userData = getRegistrationData(nonDefaultValues);
        Response responseCreateAuth = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of '"+ emptyValue +"' field is too short");
    }

    @Description("Ex15 Тест проверяет невозможность создания пользователя  с очень коротким именем в один символ")
    @DisplayName("Тест проверяет невозможность создания пользователя  с очень коротким именем в один символ")
    @Test
    public void testCreateUserWithShortName() {
        String firstName = "a";

        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", firstName);
        userData = getRegistrationData(userData);
        Response responseCreateAuth = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too short");
    }

    @Description("Ex15 Тест проверяет невозможность создания пользователя с именем, длиннее 250 символов")
    @DisplayName("Тест проверяет невозможность создания пользователя с именем, длиннее 250 символов")
    @Test
    public void testCreateUserWithLongName() {
        String firstName = "Повседневная практика показывает, что реализация намеченного плана развития требует определения" +
                           " и уточнения дальнейших направлений развития проекта. С другой стороны сложившаяся структура организации " +
                            "играет важную роль в формировании системы масштабно ";

        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", firstName);
        userData = getRegistrationData(userData);
        Response responseCreateAuth = apiCoreRequests
                .makePostRequestLoginUser("https://playground.learnqa.ru/api_dev/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too long");
    }
}
