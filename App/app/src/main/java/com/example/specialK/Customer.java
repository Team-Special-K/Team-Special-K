package com.example.specialK;

public class Customer {
    private int customerID;
    private String customerName, customerPhoneNum, customerPhone, customerPart, customerService, customerDate,
    customerDelivery, customerHavePhone, customerContacted, customerNotes;

    public Customer(int customerID, String customerName, String customerPhoneNum, String customerPhone, String customerPart, String customerService, String customerDate, String customerDelivery, String customerHavePhone, String customerContacted, String customerNotes) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerPhoneNum = customerPhoneNum;
        this.customerPhone = customerPhone;
        this.customerPart = customerPart;
        this.customerService = customerService;
        this.customerDate = customerDate;
        this.customerDelivery = customerDelivery;
        this.customerHavePhone = customerHavePhone;
        this.customerContacted = customerContacted;
        this.customerNotes = customerNotes;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNum() {
        return customerPhoneNum;
    }

    public void setCustomerPhoneNum(String customerPhoneNum) {
        this.customerPhoneNum = customerPhoneNum;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerPart() {
        return customerPart;
    }

    public void setCustomerPart(String customerPart) {
        this.customerPart = customerPart;
    }

    public String getCustomerService() {
        return customerService;
    }

    public void setCustomerService(String customerService) {
        this.customerService = customerService;
    }

    public String getCustomerDate() {
        return customerDate;
    }

    public void setCustomerDate(String customerDate) {
        this.customerDate = customerDate;
    }

    public String getCustomerDelivery() {
        return customerDelivery;
    }

    public void setCustomerDelivery(String customerDelivery) {
        this.customerDelivery = customerDelivery;
    }

    public String getCustomerHavePhone() {
        return customerHavePhone;
    }

    public void setCustomerHavePhone(String customerHavePhone) {
        this.customerHavePhone = customerHavePhone;
    }

    public String getCustomerContacted() {
        return customerContacted;
    }

    public void setCustomerContacted(String customerContacted) {
        this.customerContacted = customerContacted;
    }

    public String getCustomerNotes() {
        return customerNotes;
    }

    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
    }
}
