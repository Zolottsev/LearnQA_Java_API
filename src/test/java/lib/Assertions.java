package lib;

import io.restassured.response.Response;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
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
    public static void assertJsonByName(Response Response, String name, int expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue,value,"JSON value is not equal to expected value");
    }

        public static void assertJsonByName(Response Response, String name, String expectedValue) {
            Response.then().assertThat().body("$", hasKey(name));

            String value = Response.jsonPath().getString(name);
            assertEquals(expectedValue,value,"JSON value is not equal to expected value");
        }

    /*
    * Сравниваем текст ответа
    */
    public static void assertResponseTextEquals(Response Response, String expectedAnswer) {
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "response text is not as expected"
        );
    }
    /*
     * Сравниваем код ответа
    */
        public static void assertResponseCodeEquals(Response Response, int expectedStatusCode) {
            assertEquals(
                    expectedStatusCode,
                    Response.statusCode(),
                    "response status code is not as expected"
            );
        }

        /*
        * Проверяем, что приходит поле с определенным именем этого поля
        * */
        public static void assertJsonHasField(Response Response, String expectedFieldName) {
            Response.then().assertThat().body("$", hasKey(expectedFieldName));
        }

        /*
        * Работает аналогично assertJsonHasField, проверяет все поля через цикл for,
        * на вход принимает массив полей.
        * Внутри для каждого поля вызывается метод assertJsonHasField
        */
        public static void assertJsonHasFields(Response Response, String[] expectedFieldsName) {
            for (String expectedFieldName : expectedFieldsName)
                Assertions.assertJsonHasField(Response, expectedFieldName);
        }

        public static void assertJsonHasNotField(Response Response, String unexpectedFieldName) {
            Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
        }


}
