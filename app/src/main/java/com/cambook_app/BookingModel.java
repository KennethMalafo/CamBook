package com.cambook_app;
public class BookingModel {
    private String eventType;
    private Long downpayment;
    private Long totalPrice;
    private String status;
    private String imageUrl;

    public BookingModel(String eventType, Long downpayment, Long totalPrice, String status, String imageUrl) {
        this.eventType = eventType;
        this.downpayment = downpayment;
        this.totalPrice = totalPrice;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    public String getEventType() {
        return eventType;
    }

    public Long getDownpayment() {
        return downpayment;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }
}