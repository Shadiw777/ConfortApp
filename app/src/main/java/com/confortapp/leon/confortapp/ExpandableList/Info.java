package com.confortapp.leon.confortapp.ExpandableList;

/**
 * Created by Leon on 19.03.2018.
 */

public class Info {
    private  int id;
    private  String name;
    private  String type;

    public Info(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
