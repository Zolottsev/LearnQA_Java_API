import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class Redirect {
    /*
    *Тест создает GET-запрос на адрес: https://playground.learnqa.ru/api/long_redirect,
    * выводится конечный адрес, на который редиректит URL
    * */
    @Test
    public void testRedirect() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false).
                        baseUri("https://playground.learnqa.ru")
                .when()
                        .get("/api/long_redirect")
                .andReturn();
        String locationHeader = response.getHeader("Location");
        System.out.printf("Redirect destination address: %s\n", locationHeader);
    }
}