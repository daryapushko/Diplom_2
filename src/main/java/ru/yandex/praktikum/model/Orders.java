package ru.yandex.praktikum.model;

import java.util.ArrayList;

    public class Orders {
        private ArrayList<OrderList> orderLists;

        public ArrayList<OrderList> getOrders() {
            return orderLists;
        }

        public void setOrders(ArrayList<OrderList> orderLists) {
            this.orderLists = orderLists;
        }

        @Override
        public String toString() {
            return "Orders{" +
                    "orderLists=" + orderLists +
                    '}';
        }
    }
