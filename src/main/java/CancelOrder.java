import static io.restassured.RestAssured.given;

public class CancelOrder {
    private static final String CANCEL_ENDPOINT = "/api/v1/orders/cancel";
    public void cancelOrder(int track) {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete(CANCEL_ENDPOINT.replace("track", String.valueOf(track)));
    }
}
