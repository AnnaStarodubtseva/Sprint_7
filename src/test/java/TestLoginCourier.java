import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestLoginCourier extends BaseURI {
    private static final String DELETE_ENDPOINT = "/api/v1/courier/id";
    int idCourier;
    CreateCourier createCourier = new CreateCourier();
    LoginCourier loginCourier = new LoginCourier();

    @Before
    public void setUp() {
        RestAssured.requestSpecification = requestSpec;
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
        createCourier.createCourier().then().statusCode(201);
        // Отправляем POST-запрос на логин курьера
        loginCourier.loginCourier().then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        idCourier = loginCourier.loginCourier().path("id");
    }

    @Step("Checking the login with a missing password/login/password&login")
    public void testMissingFields() {
        // Отправляем POST-запрос на создание курьера
        createCourier.createCourier().then().statusCode(201);
        // Отправляем POST-запрос на логин курьера - проверяем его валидность
        loginCourier.loginCourier().then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        idCourier = loginCourier.loginCourier().path("id");
        // Отправляем POST-запрос на авторизацию курьера без пароля
        loginCourier.loginCourierWithoutPassword().then()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
        // Отправляем POST-запрос на авторизацию курьера без логина
        loginCourier.loginCourierWithoutLogin().then()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
        // Отправляем POST-запрос на авторизацию курьера без пароля и логина
        loginCourier.loginCourierWithoutPasswordLogin().then()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Step("Checking the login with a non-existent password/login/password&login")
    public void testWrongCredentials() {
        // Отправляем POST-запрос на создание курьера
        createCourier.createCourier().then().statusCode(201);
        // Отправляем POST-запрос на логин курьера - проверяем его валидность
        loginCourier.loginCourier().then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        idCourier = loginCourier.loginCourier().path("id");
        // Отправляем POST-запрос на авторизацию курьера с неверным паролем
        loginCourier.loginCourierWrongPassword().then()
                .assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
        // Отправляем POST-запрос на авторизацию курьера с неверным логином
        loginCourier.loginCourierWrongLogin()
                .then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
        // Отправляем POST-запрос на авторизацию курьера с неверным паролем и логином
        loginCourier.loginCourierWrongLoginPassword().then()
                .assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
}
