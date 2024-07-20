import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest extends CourierBaseTest {

    /* @Test
    public void testCreateCourier() {
        Response response = createCourier();
        response.then().statusCode(201)
                .body("ok", equalTo(true));
    } курьер создается в классе  CourierBaseTest */

    @Test
    public void testCannotCreateDuplicateCourier() {
        createCourier();
        Response response = createCourier();
        response.then().statusCode(409).and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void testCreateCourierWithoutRequiredFields() {
        Response response = createCourierWithoutLogin();
        response.then().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void testCreateCourierWithoutPassword() {
        Response response = createCourierWithoutPassword();
        response.then().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void testCreateCourierWithoutFirstName() { // из документации непонятно, first name - обязательно для заполнения???
        Response response = createCourierWithoutFirstName();
        response.then().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Creating courier without login")
    private Response createCourierWithoutLogin() {
        return given()
                .header("Content-type", "application/json")
                .body("{\"password\": \"" + PASSWORD + "\", \"firstName\": \"" + FIRST_NAME + "\"}")
                .when()
                .post(COURIER_ENDPOINT);
    }

    @Step("Creating courier without password")
    private Response createCourierWithoutPassword() {
        return given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"" + login + "\", \"firstName\": \"" + FIRST_NAME + "\"}")
                .when()
                .post(COURIER_ENDPOINT);
    }

    @Step("Creating courier without firstName")
    private Response createCourierWithoutFirstName() {
        return given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"" + login + "\", \"password\": \"" + PASSWORD + "\"}")
                .when()
                .post(COURIER_ENDPOINT);
    }
}
