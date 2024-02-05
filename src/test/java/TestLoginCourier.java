import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestLoginCourier {
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
    public void checkPositiveLoginCourier() { testSuccessfulLogin(); }
    @Test
    public void checkNegativeLoginCourierMissingFields() {
        testMissingFields();
    }
    @Test
    public void checkNegativeLoginCourierWrongCredentials() {
        testWrongCredentials();
    }

    @Step("Checking successful login")
    public void testSuccessfulLogin() {
        // Отправляем POST-запрос на создание курьера
        given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .when()
                .post(COURIER_ENDPOINT)
                .then().statusCode(201);
        // Отправляем POST-запрос на логин курьера
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

    @Step("Checking the login with a missing password/login/password&login")
    public void testMissingFields() {
        // Описываем тело запроса без указания всех обязательных полей
        String withoutPassword = "{\"login\": \"AbC\", \"password\": \"\"}";
        String withoutLogin = "{\"login\": \"\", \"password\": \"123Qwerty\"}";
        String withoutPasswordLogin = "{\"login\": \"\", \"password\": \"\"}";
        // Отправляем POST-запрос на создание курьера
        given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .when()
                .post(COURIER_ENDPOINT)
                .then().statusCode(201);
        // Отправляем POST-запрос на логин курьера - проверяем его валидность
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(LOGIN_ENDPOINT);
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        idCourier = response.path("id");
        // Отправляем POST-запрос на авторизацию курьера без пароля
        Response noPassword = given()
                .header("Content-type", "application/json")
                .body(withoutPassword)
                .when()
                .post(LOGIN_ENDPOINT);
        noPassword.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
        // Отправляем POST-запрос на авторизацию курьера без логина
        Response noLogin = given()
                .header("Content-type", "application/json")
                .body(withoutLogin)
                .when()
                .post(LOGIN_ENDPOINT);
        noLogin.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
        // Отправляем POST-запрос на авторизацию курьера без пароля и логина
        Response noPasswordLogin = given()
                .header("Content-type", "application/json")
                .body(withoutPasswordLogin)
                .when()
                .post(LOGIN_ENDPOINT);
        noPasswordLogin.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Step("Checking the login with a non-existent password/login/password&login")
    public void testWrongCredentials() {
        // Описываем тело запроса с несуществующими параметрами
        String wrongPassword = "{\"login\": \"AbC\", \"password\": \"123\"}";
        String wrongLogin = "{\"login\": \"AbCd\", \"password\": \"123Qwerty\"}";
        String wrongPasswordLogin = "{\"login\": \"AbCd\", \"password\": \"123\"}";
        // Отправляем POST-запрос на создание курьера
        given()
                .header("Content-type", "application/json")
                .body(createCourier)
                .when()
                .post(COURIER_ENDPOINT)
                .then().statusCode(201);
        // Отправляем POST-запрос на логин курьера - проверяем его валидность
        Response response = given()
                .header("Content-type", "application/json")
                .body(loginCourier)
                .when()
                .post(LOGIN_ENDPOINT);
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        idCourier = response.path("id");
        // Отправляем POST-запрос на авторизацию курьера с неверным паролем
        Response incorrectPassword = given()
                .header("Content-type", "application/json")
                .body(wrongPassword)
                .when()
                .post(LOGIN_ENDPOINT);
        incorrectPassword.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
        // Отправляем POST-запрос на авторизацию курьера с неверным логином
        Response incorrectLogin = given()
                .header("Content-type", "application/json")
                .body(wrongLogin)
                .when()
                .post(LOGIN_ENDPOINT);
        incorrectLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
        // Отправляем POST-запрос на авторизацию курьера с неверным паролем и логином
        Response incorrectPasswordLogin = given()
                .header("Content-type", "application/json")
                .body(wrongPasswordLogin)
                .when()
                .post(LOGIN_ENDPOINT);
        incorrectPasswordLogin.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
}
