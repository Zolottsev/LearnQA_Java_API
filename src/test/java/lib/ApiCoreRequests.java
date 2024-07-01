package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;
import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-request  with auth data")
    public Response makePostRequestLoginUser(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a PUT-request with auth data")
    public Response makePutRequest(String url, Map<String,String> authData, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(authData)
                .put(url)
                .andReturn();
    }

    @Step("Make a PUT-request w/o auth data")
    public Response makeNoAuthPutRequest(String url, Map<String,String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .put(url)
                .andReturn();
    }
    @Step("Make a PUT-request to edit non auth user")
    public Response makePutRequestForNoAuthUser(String url, Map<String, String> editData, String wrongToken, String wrongCookie) {
        return given()
                .filter(new AllureRestAssured())
                .body(editData)
                .header(new Header("x-csrf-token", wrongToken))
                .cookie("auth_sid", wrongCookie)
                .put(url)
                .andReturn();
    }

    @Step("Make a DELETE-request with auth data")
    public Response makeDeleteRequest(String url,  Map<String, String> deleteData, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(deleteData)
                .delete(url)
                .andReturn();
    }

    @Step("Make a DELETE-request for delete another user")
    public Response makeDeleteRequestForDeleteAnotherUser(String url, String tokenDelAnother, String cookieDelAnother) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", tokenDelAnother))
                .cookie("auth_sid", cookieDelAnother)
                .delete(url)
                .andReturn();

    }

}