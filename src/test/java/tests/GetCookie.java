package tests;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetCookie {
    @Test
    public void testGetCookie() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@examle.com");
        authData.put("password", "1234");

        Response responseGetCookie = RestAssured
                .given()
                .body(authData)
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Map<String,String> cookies = responseGetCookie.getCookies();
        System.out.println(cookies.toString());
        assertTrue(cookies.containsKey("HomeWork"), "Response doesn't have 'HomeWork' cookie");
        assertTrue(cookies.containsValue("hw_value"), "Response doesn't have cookie with value 'hw_value'");
    }
}

