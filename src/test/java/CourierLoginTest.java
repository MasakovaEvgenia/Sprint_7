import api.CourierApi;
import entity.Courier;
import entity.LoginData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {
    private static final Logger log = LoggerFactory.getLogger(CourierLoginTest.class);

    protected final String LOGIN = "ninja_" + RandomStringUtils.randomAlphanumeric(8);
    protected final String PASSWORD = "1234";
    protected final String FIRST_NAME = "saske";
    public Integer courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = CourierApi.BASE_URL;
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            CourierApi.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Testing courier can login")
    public void testCourierCanLogin() {
        Courier courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        Response createResp = CourierApi.createCourier(courier);
        LoginData login = new LoginData(LOGIN, PASSWORD);
        Response loginResponse = CourierApi.loginCourier(login);
        courierId = loginResponse.then().extract().path("id");
        loginResponse.then().statusCode(SC_OK).body("id", notNullValue());
    }

    @Test
    @DisplayName("Testing courier login with incorrect credentials")
    public void testCourierLoginWithIncorrectCredentials() {
        LoginData login = new LoginData(LOGIN, "wrongPassword");
        Response response = CourierApi.loginCourier(login);
        response.then().statusCode(SC_NOT_FOUND).body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Testing login for non-existent user")
    public void testCourierLoginNonExistentUser() {
        LoginData login = new LoginData("wrongLogin", "wrongPassword");
        Response response = CourierApi.loginCourier(login);
        response.then().statusCode(SC_NOT_FOUND).body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Testing courier login without login")
    public void testCourierLoginWithoutLogin() {
        LoginData login = new LoginData(null, "wrongPassword");
        Response response = CourierApi.loginCourier(login);
        response.then().statusCode(SC_BAD_REQUEST).body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Testing courier login without password") // Падает по таймауту. Нужно править сервис.
    public void testCourierLoginWithoutPassword() {
        LoginData login = new LoginData("wrongLogin", null);
        Response response = CourierApi.loginCourier(login);
        response.then().statusCode(SC_BAD_REQUEST).body("message", equalTo("Недостаточно данных для входа"));
    }
}
