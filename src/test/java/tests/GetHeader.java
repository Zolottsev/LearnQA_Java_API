package tests;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GetHeader {
    @Test
    public void testGetHeader() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@examle.com");
        authData.put("password", "1234");

        Response responseGetHeader = RestAssured
                .given()
                .body(authData)
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        Headers headers = responseGetHeader.getHeaders();

        Map<String, String> expectedHeadersMap = new HashMap<>();
        expectedHeadersMap.put("Date", headers.getValue("Date"));
        expectedHeadersMap.put("Content-Type", "application/json");
        expectedHeadersMap.put("Content-Length", "15");
        expectedHeadersMap.put("Connection", "keep-alive");
        expectedHeadersMap.put("Keep-Alive", "timeout=10");
        expectedHeadersMap.put("Server", "Apache");
        expectedHeadersMap.put("x-secret-homework-header", "Some secret value");
        expectedHeadersMap.put("Cache-Control", "max-age=0");
        expectedHeadersMap.put("Expires", headers.getValue("Expires"));

        Map<String, String> headersMap = new HashMap<>();
        for (Header s : headers) {
            headersMap.put(s.getName(),s.getValue());
        }
        assertEquals(expectedHeadersMap, headersMap, "Response doesn't have the expected headers");

    }
}