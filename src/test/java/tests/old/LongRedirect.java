package tests.old;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class LongRedirect {
    /*
    *Тест создает GET-запрос на адрес: https://playground.learnqa.ru/api/long_redirect,
    * Используем цикл, который будет создавать запросы в цикле,
    * каждый раз читая URL для редиректа из нужного заголовка. И так, пока мы не дойдем до ответа с кодом 200.
    * выводится конечный адрес, на который редиректит URL
    * */
  @Test
    public void testLongRedirect() {
        int statusCode = 0;
        int iterator =0;
        String locationHeader = "https://playground.learnqa.ru/api/long_redirect";
        while (statusCode !=200) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(locationHeader)
                    .andReturn();
            statusCode = response.getStatusCode();
            locationHeader = response.getHeader("Location");
            if (locationHeader!=null) {
                iterator++;
                System.out.printf("tests.old.Redirect destination address %s: %s \n", iterator, locationHeader);
            }
        }
        System.out.printf("Count of redirects: %s\n", iterator);
    }
}


