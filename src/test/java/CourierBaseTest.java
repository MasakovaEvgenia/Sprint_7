import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;

public class CourierBaseTest {
    protected final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    protected final String COURIER_ENDPOINT = "/api/v1/courier";
    protected final String LOGIN_ENDPOINT = "/api/v1/courier/login";
    protected final String DELETE_COURIER_ENDPOINT = "/api/v1/courier/";

    protected String login;
    protected final String PASSWORD = "1234";
    protected final String FIRST_NAME = "saske";

    protected Integer courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        login = generateUniqueLogin();
        createCourier();
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            deleteCourier(courierId);
        }
    }

    @Step("Generating unique login")
    protected String generateUniqueLogin() { // генерирую новый логин, чтобы не было конфликтов
        return "ninja_" + RandomStringUtils.randomAlphanumeric(8);
    }

    @Step("Creating courier")
    protected Response createCourier() {
        Response response = given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"" + login + "\", \"password\": \"" + PASSWORD + "\", \"firstName\": \"" + FIRST_NAME + "\"}")
                .when()
                .post(COURIER_ENDPOINT);

        courierId = response.then().extract().path("id");
        return response;
    }

    @Step("Deleting courier with id: {courierId}")
    protected Response deleteCourier(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(DELETE_COURIER_ENDPOINT + courierId);
    }

    @Step("Logging in courier with login: {login} and password: {password}")
    protected Response loginCourier(String login, String password) {
        String loginData = "{ \"login\": \"" + login + "\", \"password\": \"" + password + "\" }";

        return given()
                .header("Content-type", "application/json")
                .body(loginData)
                .when()
                .post(LOGIN_ENDPOINT);
    }
}
