package com.example.navigationdrawer;

import com.example.navigationdrawer.helperclasses.TaskBodyObject;

import java.util.ArrayList;

public class ExpListGroup {

    public String text;
//    public ArrayList<ExpListItem> items = new ArrayList<>();
    public ArrayList<TaskBodyObject> items = new ArrayList<>();

    public ExpListGroup(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
