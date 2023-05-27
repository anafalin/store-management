package ru.lazarenko.storemanagement.util;


import ru.lazarenko.storemanagement.model.OrderStatus;

public class StatusUtils {

    public static OrderStatus getOrderStatus(String name) {
        return switch (name) {
            case "new" -> OrderStatus.NEW;
            case "accepted" -> OrderStatus.ACCEPTED;
            case "rejected" -> OrderStatus.REJECTED;
            case "finished" -> OrderStatus.FINISHED;
            default -> null;
        };
    }
}
