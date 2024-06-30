package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
    /*
    * Класс не является прямым наследником для наших тестов.
    * Функции делаем статическими, чтобы каждый раз не создавать объект Assertions
    */
public class Assertions {
    /*
    * На вход получаем объект с ответом сервера, имя и ожидаемое значение
    * Убеждаемся, что значение внутри JSON доступно по определенному имени и равняется ожидаемому значению.
    * На выходе assert, где сравниваем ожидаемое значение и полученное из json
    * если правильное-проходит, если нет - сообщение
    */
    public static void assertJsonByName (Response Response, String name, int expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue,value,"JSON value is not equal to expected value");
    }
}
