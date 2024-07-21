package entity;

public class AcceptOrderData {

    final Integer courierId;
    final Integer orderId;

    public AcceptOrderData(Integer courierId, Integer orderId) {
        this.courierId = courierId;
        this.orderId = orderId;
    }

    public Integer getCourierId() {
        return courierId;
    }

    public Integer getOrderId() {
        return orderId;
    }
}
