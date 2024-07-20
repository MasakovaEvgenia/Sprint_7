import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTest {
    private final String[] colors;

    public OrderTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        });
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Step
    public Response createOrder(String[] colors) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("firstName", "Naruto");
        requestBody.put("lastName", "Uchiha");
        requestBody.put("address", "Konoha, 142 apt.");
        requestBody.put("metroStation", 4);
        requestBody.put("phone", "+7 800 355 35 35");
        requestBody.put("rentTime", 5);
        requestBody.put("deliveryDate", "2020-06-06");
        requestBody.put("comment", "Saske, come back to Konoha");
        requestBody.put("color", colors);

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody.toString())
                .when()
                .post("/api/v1/orders");
    }

    @Step
    public void validateResponseBodyContainsTrack(Response response) {
        response.then().body("track", notNullValue());
    }

    @Test
    public void testCreateOrder() {
        Response response = createOrder(colors);
        validateStatusCode(response, 201);
        validateResponseBodyContainsTrack(response);
    }

    private void validateStatusCode(Response response, int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }
}
