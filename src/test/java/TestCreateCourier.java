import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestCreateCourier {
    private static final String COURIER_ENDPOINT = "/api/v1/courier";
    private static final String DELETE_ENDPOINT = "/api/v1/courier/id";
    private static final String LOGIN_ENDPOINT = "/api/v1/courier/login";
    String createCourier = "{\"login\": \"AbC\", \"password\": \"123Qwerty\", \"firstName\": \"aBcDeFgH\"}";
    String loginCourier = "{\"login\": \"AbC\", \"password\": \"123Qwerty\"}";
    int idCourier;

    @Before
    public void setURL() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }
    @After
    public void deleteCourier() {
        given()
                .header("Content-type", "application/json")
                .when()
                .delete(DELETE_ENDPOINT.replace("id", String.valueOf(idCourier)));
    }

    @Test
    public void checkPositiveCourierCreation() {
        testCourierCreation();
    }
    @Test
    public void checkNegativeCourierCreation() {
        testDuplicateCourierCreation();
        testMissingFields();
    }


    @Step("Create a courier and check the successful creation through the Courier Login")
    public void testCourierCreation() {
        // Отправляем POST-запрос на создание курьера
        Response responseCreate = given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .when()
                .post(COURIER_ENDPOINT);
        responseCreate.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
        // Отправляем POST-запрос на логин созданного курьера - исключаем ложно-положительные тесты
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(LOGIN_ENDPOINT);
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        idCourier = response.path("id");
    }

    @Step("Check that it is impossible to create two identical couriers")
    public void testDuplicateCourierCreation() {
        // Отправляем POST-запрос на создание курьера
        given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .when()
                .post(COURIER_ENDPOINT)
                .then().statusCode(201);
        // Отправляем POST-запрос на логин курьера - убеждаемся, что курьер точно создан
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(LOGIN_ENDPOINT);
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        idCourier = response.path("id");
        // Отправляем POST-запрос на создание такого же курьера
        Response responseDublicate = given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .when()
                .post(COURIER_ENDPOINT);
        responseDublicate.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Step("Check that in order to create a courier, you need to fill in all required fields")
    public void testMissingFields() {
        // Описываем тело запроса для создания курьера без указания обязательных полей
        String withoutPassword = "{\"login\": \"AbC\", \"password\": \"\", \"firstName\": \"aBcDeFgH\"}";
        String withoutLogin = "{\"login\": \"\", \"password\": \"123Qwerty\", \"firstName\": \"aBcDeFgH\"}";

        // Отправляем POST-запрос на создание курьера без пароля
        Response noPassword = given()
                .header("Content-type", "application/json")
                .body(withoutPassword)
                .when()
                .post(COURIER_ENDPOINT);
        noPassword.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
        // Отправляем POST-запрос на создание курьера без логина
        Response noLogin = given()
                .header("Content-type", "application/json")
                .body(withoutLogin)
                .when()
                .post(COURIER_ENDPOINT);
        noLogin.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

}
