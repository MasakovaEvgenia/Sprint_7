import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest extends CourierBaseTest {
    private static final Logger log = LoggerFactory.getLogger(CourierLoginTest.class);

    @Test
    @DisplayName("Testing courier can login")
    public void testCourierCanLogin() {
        Response response = loginCourier(login, PASSWORD);
        log.info(String.valueOf(response));
        response.then().statusCode(200).body("id", notNullValue());
    }

    @Test
    @DisplayName("Testing courier login with incorrect credentials")
    public void testCourierLoginWithIncorrectCredentials() {
        Response response = loginCourier("wrongLogin", "wrongPassword");
        response.then().statusCode(404).body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Testing login for non-existent user")
    public void testCourierLoginNonExistentUser() {
        Response response = loginCourier("nonExistentUser", "1234");
        response.then().statusCode(404).body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Testing courier login without login")
    public void testCourierLoginWithoutLogin() {
        Response response = given()
                .header("Content-type", "application/json")
                .body("{ \"password\": \"" + PASSWORD + "\" }")
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"))
                .extract()
                .response();
        log.info(String.valueOf(response));
    }

    @Test
    @DisplayName("Testing courier login without password") // Падает по таймауту. Нужно править сервис.
    public void testCourierLoginWithoutPassword() {
        Response response = given()
                .header("Content-type", "application/json")
                .body("{ \"login\": \"" + login + "\" }")
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"))
                .extract()
                .response();
        log.info(String.valueOf(response));
    }
}
