package com.ruparts;

import com.ruparts.helperclasses.TaskObject;

import java.util.ArrayList;

public class ExpListGroup {

    public String elgTaskType;
    public String elgTaskTypeToShow;
    public ArrayList<TaskObject> itemsList = new ArrayList<>();

    public ExpListGroup(String elgTaskType) {
        this.elgTaskType = elgTaskType;
    }

    public void updateTask(TaskObject task) {
        for (int i = 0; i < itemsList.size(); i++) {
            if (itemsList.get(i).taskId == task.taskId) {
                itemsList.set(i, task);
            }
        }
    }
    
}
