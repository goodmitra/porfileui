package com.namdev.namdevvivah;

public class Mass {
    private final String Message;
    private final  String leftMsg;
    private final  String rightMsg;
    private final  String image;
    private final  String type;

    public Mass(String id, String left, String right, String img,String type) {
        this.Message = id;
        this.leftMsg = left;
        this.rightMsg = right;
        this.image = img;
        this.type = type;
    }

    public String getId() {
        return Message;
    }

    public String getSender() { return rightMsg; }
    public String getType() { return type; }

    public String getRecive() {
        return leftMsg ;
    }
    public String getImage() {
        return image ;
    }
}
