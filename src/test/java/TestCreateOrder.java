import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class TestCreateOrder {
    private static final String ORDER_ENDPOINT = "/api/v1/orders";
    private static final String CANCEL_ENDPOINT = "/api/v1/orders/cancel";
    int track;
    private String[] color;

    public TestCreateOrder(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getColor() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        };
    }
    @Before
    public void setURL() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void deleteCourier() {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete(CANCEL_ENDPOINT.replace("track", String.valueOf(track)));
    }

    @Test
    public void testOrderCreation() {
        // Описываем тело запроса
        String createOrder = "{\"firstname\":\"Анна\",\"lastname\":\"Шакина\",\"address\":\"Линия, 142\",\"metrostation\":4,\"phone\":\"+7 900 355 35 35\",\"renttime\":5,\"deliverydate\":\"2020-06-06\",\"comment\":\"saske, come back to konoha\",\"color\":[\"" + color + "\"]}";
        // Отправляем POST-запрос на создание заказа
        Response response = given()
                .header("Content-type", "application/json")
                .body(createOrder)
                .when()
                .post(ORDER_ENDPOINT);
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
        track = response.path("track");
    }
}

