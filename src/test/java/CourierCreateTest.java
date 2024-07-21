import api.CourierApi;
import entity.Courier;
import entity.LoginData;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest {

    protected final String LOGIN =  "ninja_" + RandomStringUtils.randomAlphanumeric(8);
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
    public void testCreateCourier() {
        Courier courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        Response response = CourierApi.createCourier(courier);
        courierId = response.then().extract().path("id");
        response.then().statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    public void testCannotCreateDuplicateCourier() {
        Courier courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        Response response = CourierApi.createCourier(courier);
        LoginData loginData = new LoginData(LOGIN, PASSWORD);
        Response loginResp = CourierApi.loginCourier(loginData);
        courierId = loginResp.then().extract().path("id");
        Response responseDuplicate = CourierApi.createCourier(courier);
        responseDuplicate.then().statusCode(SC_CONFLICT).and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void testCreateCourierWithoutLogin() {
        Courier courier = new Courier(null, PASSWORD, FIRST_NAME);
        Response response = CourierApi.createCourier(courier);
        response.then().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void testCreateCourierWithoutPassword() {
        Courier courier = new Courier(LOGIN, null, FIRST_NAME);
        Response response = CourierApi.createCourier(courier);
        response.then().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void testCreateCourierWithoutFirstName() { // из документации непонятно, first name - обязательно для заполнения???
        Courier courier = new Courier(LOGIN, PASSWORD, null);
        Response response = CourierApi.createCourier(courier);
        response.then().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

}
