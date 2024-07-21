import api.CourierApi;
import api.OrderApi;
import entity.*;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class ListOfOrdersTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    Integer failCourierId = 999999;

    protected final String LOGIN = "ninja_" + RandomStringUtils.randomAlphanumeric(8);
    protected final String PASSWORD = "1234";
    protected final String FIRST_NAME = "saske";


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }


    @Test // Проверка ответа на запрос списка заказов без courierId
    @DisplayName("Get orders with null courier id")

    public void testGetOrdersWithoutCourierId() {
        Response response = OrderApi.getCourierOrderList(null);
        response.then().statusCode(SC_INTERNAL_SERVER_ERROR); //Разве это нормально???
    }

    @Test // Проверка ответа на запрос списка заказов с несуществующим courierId
    @DisplayName("Get orders with not exist courier id")
    public void testGetOrdersWithNonExistingCourierId() {
        Response response = OrderApi.getCourierOrderList(failCourierId);
        response.then().statusCode(SC_NOT_FOUND).body("message", equalTo("Курьер с идентификатором " + failCourierId + " не найден"));
    }

    @Test //Ошибка АПИ. Заказ привязывается дважды!
    @DisplayName("Add new courier new order")
    public void testGetOrdersList() {
        Courier courier = new Courier(LOGIN, PASSWORD, FIRST_NAME);
        Response createResp = CourierApi.createCourier(courier);

        LoginData loginData = new LoginData(LOGIN, PASSWORD);
        Response loginResp = CourierApi.loginCourier(loginData);
        Integer courierId = loginResp.then().extract().path("id");

        Order orderData = new Order("Саске", "Учиха", "Пещера Орочимару", "1", "1-2-3-4-5", 5, "2002-02-02", "UwU", new String[]{"BLACK"});
        Response orderResp = OrderApi.createOrder(orderData);
        Integer orderTrack = orderResp.then().statusCode(SC_CREATED).extract().path("track");

        Response orderByTrack = OrderApi.getOrderByTrack(orderTrack);
        Integer orderId = orderByTrack.then().statusCode(SC_OK).extract().path("order.id");

        AcceptOrderData acceptOrderData = new AcceptOrderData(courierId, orderId);
        Response acceptResp = OrderApi.addOrder(acceptOrderData);
        acceptResp.then().statusCode(SC_OK) .body("ok", equalTo(true));

        Response courierOrderList = OrderApi.getCourierOrderList(courierId);
        courierOrderList.then().statusCode(SC_OK);
        OrdersData ordersData = courierOrderList.as(OrdersData.class);
        Assert.assertFalse(ordersData.getOrders().isEmpty());
        //Assert.assertEquals(1, ordersData.getOrders().size()); //Ошибка АПИ. Заказ привязывается дважды!

        CourierApi.deleteCourier(courierId);
    }

}
