import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;

public class TestListOrders {
    private static final String LIST_ORDERS_ENDPOINT = "/api/v1/orders";

    @Before
    public void setURL() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void testListOrders() {
        given()
                .get(LIST_ORDERS_ENDPOINT)
                .then().assertThat().body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

} 