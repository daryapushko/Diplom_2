package ru.yandex.praktikum.model;

import java.util.ArrayList;

public class OrderListResponse {
    private boolean success;
    public ArrayList<Object> orders;
    private int total;
    private int totalToday;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<Object> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Object> orders) {
        this.orders = orders;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalToday() {
        return totalToday;
    }

    public void setTotalToday(int totalToday) {
        this.totalToday = totalToday;
    }

    @Override
    public String toString() {
        return "OrderListResponse{" +
                "success=" + success +
                ", orders=" + orders +
                ", total=" + total +
                ", totalToday=" + totalToday +
                '}';
    }
}
