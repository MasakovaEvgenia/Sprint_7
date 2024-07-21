package entity;

import java.util.List;

public class OrdersData {
    private final List<Order> orders;

    public OrdersData(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
