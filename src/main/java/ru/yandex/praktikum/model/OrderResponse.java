package ru.yandex.praktikum.model;

public class OrderResponse {
    private boolean success;
    private String name;
    private Order order;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "success=" + success +
                ", name='" + name + '\'' +
                ", orderList=" + order +
                '}';
    }
}
