import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class Tokens {
    /*
     *Тест создает GET-запрос на адрес:  https://playground.learnqa.ru/ajax/api/longtime_job,
     *Запрос createJob создает задачу,
     *Запрос responseGetStatus делает один запрос с token до того, как задача готова, убеждается в правильности поля status,
     *Запрос responseGetResult ждет нужное количество секунд с помощью функции Thread.sleep(),
     *делает один запрос c token ПОСЛЕ того, как задача готова, убеждается в правильности поля status и наличии поля result
     */
    @Test
    public void testTokens() {
        JsonPath createJob = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String token = createJob.get("token");
        int seconds = createJob.get("seconds");
        if (token == null){
            System.out.println("The key 'token' is absent");
        } else {

            JsonPath responseGetStatus = RestAssured
                    .given()
                    .queryParam("token", token)
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .jsonPath();
            String statusBeforeJobEnd = responseGetStatus.get("status");
            System.out.printf("Статус до того, как задача готова: %s\n", statusBeforeJobEnd);

            try {
                Thread.sleep((long)seconds*1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

        JsonPath responseGetResult = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String status = responseGetResult.get("status");
        String result = responseGetResult.get("result");
        System.out.printf("Статус поле готовности задачи: %s\n", status);
        System.out.printf("Результат: %s\n", result);
        }
    }
}
