import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class TestCreateOrder extends BaseURI {
    int track;
    CancelOrder cancelOrder = new CancelOrder();
    CreateOrder createOrder = new CreateOrder();
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
        cancelOrder.cancelOrder(track);
    }


    @Test
    public void testOrderCreation() {
        createOrder.createOrder(selectColor).then()
                .assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
        track = createOrder.createOrder(selectColor).path("track");
    }
}

