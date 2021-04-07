package com.quadrant.request;
/*
* Class for retrofit request for payment session establish
* Adyen payment gateway session establish class
* This request will be forwarded to squadron server
* Author: Raghu B
* Date: 27/04/2019
* */
public class PaymentSessionRequest {

    String token;
    String channel;
    String returnUrl;






    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }



}
