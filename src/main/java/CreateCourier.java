import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CreateCourier {
    private static final String COURIER_ENDPOINT = "/api/v1/courier";
    Courier createCourier = new Courier("AbC", "123Qwerty", "aBcDeFgH");
    Courier withoutLogin = new Courier("", "123Qwerty", "aBcDeFgH");
    Courier withoutPassword = new Courier("AbC", "", "aBcDeFgH");

    public Response createCourier() {
        // Отправляем POST-запрос на создание курьера
        Response response = given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .when()
                .post(COURIER_ENDPOINT);
        return response;
    }
    public Response createCourierWithoutLogin() {
        // Отправляем POST-запрос на создание курьера без логина
        Response response = given()
                .header("Content-type", "application/json")
                .body(withoutLogin)
                .when()
                .post(COURIER_ENDPOINT);
        return response;
    }
    public Response createCourierWithoutPassword() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(withoutPassword)
                .when()
                .post(COURIER_ENDPOINT);
        return response;
    }
}
