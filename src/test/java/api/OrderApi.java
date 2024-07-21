package api;

import entity.AcceptOrderData;
import entity.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi {
    public final static String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    public final static String ORDER_ENDPOINT = "/api/v1/orders";
    public final static String ACCEPT_ORDER_ENDPOINT = "/api/v1/orders/accept/";
    public final static String ORDER_TRACK_ENDPOINT = "/api/v1/orders/track";


    @Step("Creating order")
    public static Response createOrder (Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDER_ENDPOINT);
    }

    @Step("Add order to courier")
    public static Response addOrder (AcceptOrderData data) {
        String parametrizedAcceptEndpoint = ACCEPT_ORDER_ENDPOINT + data.getOrderId() + "?courierId=" + data.getCourierId();
        return given()
                .header("Content-type", "application/json")
                .when()
                .put(parametrizedAcceptEndpoint);
    }

    @Step("get order by track")
    public static Response getOrderByTrack (Integer orderId) {
        String parametrizedEndpoint = ORDER_TRACK_ENDPOINT + "?t=" + orderId;
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(parametrizedEndpoint);
    }


    @Step("get courier order list")
    public static Response getCourierOrderList (Integer courierId) {
        String parametrizedEndpoint = ORDER_ENDPOINT + "?courierId=" + courierId;
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(parametrizedEndpoint);
    }

}
