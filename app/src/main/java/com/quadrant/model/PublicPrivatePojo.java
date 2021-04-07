package com.quadrant.model;

public class PublicPrivatePojo {

    private  int id;
    private  String PrivateorPublic;


    public PublicPrivatePojo(int id, String PrivateorPublic) {
        this.id = id;
        this.PrivateorPublic =PrivateorPublic;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrivateorPublic() {
        return PrivateorPublic;
    }

    public void setPrivateorPublic(String privateorPublic) {
        PrivateorPublic = privateorPublic;
    }
}
