package com.example.navigationdrawer;

import com.example.navigationdrawer.helperclasses.LibraryMaps;
import com.example.navigationdrawer.helperclasses.TaskBodyObject;

import java.util.ArrayList;

public class ExpListGroup {

    public String elgTaskType;
    public String elgTaskTypeToShow;
    public ArrayList<TaskBodyObject> items = new ArrayList<>();

    public ExpListGroup(String elgTaskType) {
        this.elgTaskType = elgTaskType;
    }

}
