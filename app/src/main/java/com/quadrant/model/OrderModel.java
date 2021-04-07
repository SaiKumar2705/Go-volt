package com.quadrant.model;

public class OrderModel {


   private String status,datatime,creditprice,rewardpriceval,fromtime,tilltime;

    public OrderModel(String status, String datatime, String creditprice, String rewardpriceval, String fromtime, String tilltime) {


        this.status=status;

        this.datatime=datatime;

        this.creditprice=creditprice;
        this.rewardpriceval= rewardpriceval;
        this.fromtime=fromtime;
        this.tilltime=tilltime;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatatime() {
        return datatime;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }

    public String getCreditprice() {
        return creditprice;
    }

    public void setCreditprice(String creditprice) {
        this.creditprice = creditprice;
    }

    public String getRewardpriceval() {
        return rewardpriceval;
    }

    public void setRewardpriceval(String rewardpriceval) {
        this.rewardpriceval = rewardpriceval;
    }

    public String getFromtime() {
        return fromtime;
    }

    public void setFromtime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getTilltime() {
        return tilltime;
    }

    public void setTilltime(String tilltime) {
        this.tilltime = tilltime;
    }
}
