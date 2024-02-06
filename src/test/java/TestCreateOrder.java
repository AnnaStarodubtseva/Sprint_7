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
public class TestCreateOrder extends BaseURI {
    private static final String ORDER_ENDPOINT = "/api/v1/orders";
    private static final String CANCEL_ENDPOINT = "/api/v1/orders/cancel";
    int track;
    private String[] selectColor;

    public TestCreateOrder(String[] selectColor) {
        this.selectColor = selectColor;
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
    public void setUp() {
        RestAssured.requestSpecification = requestSpec;
    }

    @After
    public void cancelOrder() {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete(CANCEL_ENDPOINT.replace("track", String.valueOf(track)));
    }

    @Test
    public void testOrderCreation() {
        // Описываем тело запроса
        Order createOrder = new Order("Анна","Шакина","Линия, 142", 4,"+7 900 355 35 35",5,"2020-06-06","saske, come back to konoha",selectColor);
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

