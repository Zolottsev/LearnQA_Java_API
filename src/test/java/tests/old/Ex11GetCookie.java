package tests.old;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex11GetCookie {
    @Test
    public void testGetCookie() {
            Response responseGetCookie = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Map<String,String> cookies = responseGetCookie.getCookies();
        System.out.println(cookies.toString());

        assertTrue(cookies.containsKey("HomeWork"), "Response doesn't have 'HomeWork' cookie");
        assertTrue(cookies.containsValue("hw_value"), "Response doesn't have cookie with value 'hw_value'");
    }
}

