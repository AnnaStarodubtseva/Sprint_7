import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestCreateCourier extends BaseURI {

    int idCourier;
    CreateCourier createCourier = new CreateCourier();
    LoginCourier loginCourier = new LoginCourier();
    DeleteCourier deleteCourier = new DeleteCourier();

    @Before
    public void setUp() {
        RestAssured.requestSpecification = requestSpec;
    }
    @After
    public void deleteCourier() {
        deleteCourier.deleteCourier(idCourier);
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
        createCourier.createCourier().then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
        // Отправляем POST-запрос на логин созданного курьера - исключаем ложно-положительные тесты
        loginCourier.loginCourier().then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        idCourier = loginCourier.loginCourier().path("id");
    }

    @Step("Check that it is impossible to create two identical couriers")
    public void testDuplicateCourierCreation() {
        // Отправляем POST-запрос на создание курьера
        createCourier.createCourier().then().statusCode(201);
        // Отправляем POST-запрос на логин курьера - убеждаемся, что курьер точно создан
        loginCourier.loginCourier().then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
        idCourier = loginCourier.loginCourier().path("id");
        // Отправляем POST-запрос на создание такого же курьера
        createCourier.createCourier().then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Step("Check that in order to create a courier, you need to fill in all required fields")
    public void testMissingFields() {
        // Отправляем POST-запрос на создание курьера без пароля
        createCourier.createCourierWithoutPassword().then()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
        // Отправляем POST-запрос на создание курьера без логина
        createCourier.createCourierWithoutLogin().then()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

}
