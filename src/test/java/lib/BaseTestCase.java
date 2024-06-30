package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {
    /*
    * Передаем headers, убеждаемся, что в соответствующие поля пришли значения.
    * Если есть - возвращаем, если нет - тест будет падать
    */
    protected String getHeader (Response Response, String name){
        Headers headers = Response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "Response doesn't have header with name" + name);
        return  headers.getValue(name);
    }

    protected String getCookie (Response Response, String name){
        Map<String, String> cookies = Response.getCookies();

        assertTrue(cookies.containsKey(name), "Response doesn't have cookie");
        return  cookies.get(name);
    }

    /*
     * Убеждаемся, что в Json есть нужное поле.
     * "$" указывает, что мы ищем ключ в корне JSON,
     *  если он спрятан за вложенностями - пишем путь
     * Если есть - возвращаем, если нет - тест будет падать
     */
    protected int getIntFromJson (Response Response, String name){
        Response.then().assertThat().body("$", hasKey(name));
        return Response.jsonPath().getInt(name);
    }
}
