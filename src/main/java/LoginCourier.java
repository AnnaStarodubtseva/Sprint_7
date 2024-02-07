import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class LoginCourier {
    private static final String LOGIN_ENDPOINT = "/api/v1/courier/login";
    Login loginCourier = new Login("AbC", "123Qwerty");
    Login withoutPassword = new Login("AbC", "");
    Login withoutLogin = new Login("", "123Qwerty");
    Login withoutPasswordLogin = new Login("", "");
    Login wrongPassword = new Login("AbC", "123");
    Login wrongLogin = new Login("AbCd", "123Qwerty");
    Login wrongPasswordLogin = new Login("AbCd", "123");

    public Response loginCourier() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(LOGIN_ENDPOINT);
        return response;
    }
    public Response loginCourierWithoutPassword() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(withoutPassword)
                .when()
                .post(LOGIN_ENDPOINT);
        return response;
    }
    public Response loginCourierWithoutLogin() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(withoutLogin)
                .when()
                .post(LOGIN_ENDPOINT);
        return response;
    }
    public Response loginCourierWithoutPasswordLogin() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(withoutPasswordLogin)
                .when()
                .post(LOGIN_ENDPOINT);
        return response;
    }
    public Response loginCourierWrongPassword() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(wrongPassword)
                .when()
                .post(LOGIN_ENDPOINT);
        return response;
    }
    public Response loginCourierWrongLogin() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(wrongLogin)
                .when()
                .post(LOGIN_ENDPOINT);
        return response;
    }
    public Response loginCourierWrongLoginPassword() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(wrongPasswordLogin)
                .when()
                .post(LOGIN_ENDPOINT);
        return response;
    }

}
