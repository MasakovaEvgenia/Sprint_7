import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ListOfOrdersTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private static final String ORDERS_ENDPOINT = "/api/v1/orders";
    String failCourierId = "999999";


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Step
    private Response sendGetRequest(String endpoint) {
        return given()
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    @Test // Проверка успешного ответа на запрос списка заказов без courierId
    public void testGetOrdersWithoutCourierId() {
        Response response = sendGetRequest(ORDERS_ENDPOINT);
        validateSuccessfulResponse(response);
        validateOrdersListInResponse(response);
    }

    @Test // Проверка ответа на запрос списка заказов с несуществующим courierId
    public void testGetOrdersWithNonExistingCourierId() {
        Response response = sendGetRequest(ORDERS_ENDPOINT + "?courierId=" + failCourierId);
        validateNotFoundResponse(response);
    }

    @Step //Проверка успешного ответа (код 200)
    private void validateSuccessfulResponse(Response response) {
        response.then().statusCode(200);
    }

    @Step //Проверка, что в ответе содержится список заказов
    private void validateOrdersListInResponse(Response response) {
        response.then().body("orders", is(not(empty())));
    }

    @Step //Проверка ответа с кодом 404
    private void validateNotFoundResponse(Response response) {
        response.then().statusCode(404)
                .body("message", equalTo("Курьер с идентификатором " + failCourierId + " не найден"));
    }
}
