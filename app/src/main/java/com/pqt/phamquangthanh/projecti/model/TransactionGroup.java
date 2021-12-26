package com.pqt.phamquangthanh.projecti.model;

import java.io.Serializable;

public class TransactionGroup implements Serializable {
    private int id;
    private String groupName;
    private int icon;
    private int typeGroup;

    public TransactionGroup(int id, String groupName, int icon, int typeGroup) {
        this.id = id;
        this.groupName = groupName;
        this.icon = icon;
        this.typeGroup = typeGroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(int typeGroup) {
        this.typeGroup = typeGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }



}
