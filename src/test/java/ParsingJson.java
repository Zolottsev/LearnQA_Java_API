import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class ParsingJson {
    /*
     *Тест делает GET-запрос на адрес https://playground.learnqa.ru/api/get_json_homework
     * и выводит текст второго сообщения.
     * */
    @Test
    public void testParsingJson() {
        /*
         * Используем экзекьютор jsonPath(). В качестве ответа получаем объект JsonPath, с помощью которого будем работать с JSON
         * */
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        ArrayList<String> messages = response.get("messages");
        if (messages == null) {
            System.out.println("The message is absent");
        } else {
            System.out.printf("The second message: %s\n",  messages.get(1));
                }
        }
    }

