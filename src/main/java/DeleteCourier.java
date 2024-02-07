import static io.restassured.RestAssured.given;

public class DeleteCourier {
    private static final String DELETE_ENDPOINT = "/api/v1/courier/id";
    public void deleteCourier(int idCourier) {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete(DELETE_ENDPOINT.replace("id", String.valueOf(idCourier)));
    }
}
