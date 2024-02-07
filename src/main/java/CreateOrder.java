import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CreateOrder {
    private static final String ORDER_ENDPOINT = "/api/v1/orders";

    public Response createOrder (String[] selectColor) {
        Order createOrder = new Order("Анна","Шакина","Линия, 142", 4,"+7 900 355 35 35",5,"2020-06-06","saske, come back to konoha",selectColor);
        Response response = given()
                .header("Content-type", "application/json")
                .body(createOrder)
                .when()
                .post(ORDER_ENDPOINT);
        return response;
    }
}
