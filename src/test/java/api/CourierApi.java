package api;

import entity.Courier;
import entity.LoginData;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi {
    public final static String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    public final static String COURIER_ENDPOINT = "/api/v1/courier";
    public final static String LOGIN_ENDPOINT = "/api/v1/courier/login";
    public final static String DELETE_COURIER_ENDPOINT = "/api/v1/courier/";

    public String login;
    public final String PASSWORD = "1234";
    public final String FIRST_NAME = "saske";


    @Step("Creating courier")
    public static Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_ENDPOINT);
    }

    @Step("Deleting courier with id: {courierId}")
    public static Response deleteCourier(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(DELETE_COURIER_ENDPOINT + courierId);
    }

    @Step("Logging in courier with login: {login} and password: {password}")
    public static Response loginCourier(LoginData login) {
        return given()
                .header("Content-type", "application/json")
                .body(login)
                .when()
                .post(LOGIN_ENDPOINT);
    }

}
