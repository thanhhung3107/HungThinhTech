package com.poly.enums;

public enum OrderStatus {
    PENDING(1, "Chờ xử lý"),
    PAID(2, "Đã thanh toán"),
    FAILED(3, "Thất bại"),
    CANCELLED(4, "Đã hủy");

    private final int code;
    private final String description;

    OrderStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatus fromCode(int code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.code == code) return status;
        }
        return null;
    }
}
