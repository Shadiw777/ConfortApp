package com.confortapp.leon.confortapp.ExpandableList;

/**
 * Created by Leon on 19.03.2018.
 */

public class ViewType {

    private int dataIndex;
    private int type;

    public ViewType(int dataIndex, int type) {
        this.dataIndex = dataIndex;
        this.type = type;
    }

    public int getDataIndex() {
        return dataIndex;
    }

    public int getType() {
        return type;
    }

}
