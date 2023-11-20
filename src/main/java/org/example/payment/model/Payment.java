package org.example.payment.model;

import java.util.Date;

public class Payment {
    private int id;
    private int amount;

    public Payment(int amount, Date date, PaymentState paymentState, int billId) {
        this.amount = amount;
        this.paymentDate = date;
        this.paymentState = paymentState;
        this.billId = billId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    private Date paymentDate;

    private int billId;

    public PaymentState getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }

    private PaymentState paymentState;
}
