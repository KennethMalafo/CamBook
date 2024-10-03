package com.cambook_app;

public class BookingSummary {
    private String eventType;
    private String packageName;
    private String province;
    private String city;
    private String barangay;
    private String selectedDate;
    private int downpayment;
    private int totalPrice;
    private String downpaymentImageUrl;
    private String userID;
    private String status;
    private String bookingID;

    // Constructor
    public BookingSummary() {
        // Default constructor required for calls to DataSnapshot.getValue(BookingSummary.class)
    }

    public BookingSummary(String eventType, String packageName, String province, String city, String barangay, String selectedDate, int downpayment, int totalPrice, String downpaymentImageUrl, String userID, String status, String bookingID) {
        this.eventType = eventType;
        this.packageName = packageName;
        this.province = province;
        this.city = city;
        this.barangay = barangay;
        this.selectedDate = selectedDate;
        this.downpayment = downpayment;
        this.totalPrice = totalPrice;
        this.downpaymentImageUrl = downpaymentImageUrl;
        this.userID = userID;
        this.status = status;
        this.bookingID = bookingID;
    }

    // Getters and setters

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public int getDownpayment() {
        return downpayment;
    }

    public void setDownpayment(int downpayment) {
        this.downpayment = downpayment;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDownpaymentImageUrl() {
        return downpaymentImageUrl;
    }

    public void setDownpaymentImageUrl(String downpaymentImageUrl) {
        this.downpaymentImageUrl = downpaymentImageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }
}
