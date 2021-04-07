package com.quadrant.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("paymentSession")
    @Expose
    String paymentSession;

    public String getPaymentSession() {
        return paymentSession;
    }

    public void setPaymentSession(String paymentSession) {
        this.paymentSession = paymentSession;
    }
}
