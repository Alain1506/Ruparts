package com.example.navigationdrawer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationdrawer.helperclasses.TaskBodyObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context c;
    private ArrayList<ExpListGroup> groups;
    private LayoutInflater inflater;

    public ExpandableListAdapter(Context c, ArrayList<ExpListGroup> groups) {
        this.c = c;
        this.groups = groups;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int parentPosition) {
        return groups.get(parentPosition).items.size();
    }

    @Override
    public Object getGroup(int parentPosition) {
        return groups.get(parentPosition);
    }

    @Override
    public Object getChild(int parentPosition, int childPosition) {
        return groups.get(parentPosition).items.get(childPosition);
    }

    @Override
    public long getGroupId(int parentPosition) {
        return 0;
    }

    @Override
    public long getChildId(int parentPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int parentPosition, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_group, null);
        }
        ExpListGroup elg = (ExpListGroup) getGroup(parentPosition);

        TextView task = view.findViewById(R.id.lbListHeader);

        String newTask = elg.text;
        task.setText(newTask);

        return view;
    }

    @Override
    public View getChildView(int parentPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, null);
        }

//        String child = (String) getChild(parentPosition, childPosition);
//        String group = getGroup(parentPosition).toString();

//        ExpListItem eli = (ExpListItem) getChild(parentPosition, childPosition);
        TaskBodyObject tbo = (TaskBodyObject) getChild(parentPosition, childPosition);

//        ImageView eliPriority = view.findViewById(R.id.item_priority);
//        TextView eliName = view.findViewById(R.id.item_name);
//        TextView eliComment = view.findViewById(R.id.item_comment);
//        TextView eliDate = view.findViewById(R.id.item_date);

        ImageView tboPriority = view.findViewById(R.id.item_priority);
        TextView tboName = view.findViewById(R.id.item_name);
        TextView tboComment = view.findViewById(R.id.item_comment);
        TextView tboDate = view.findViewById(R.id.item_date);

//        String newPriority = eli.priority;
//        String newName = eli.task;
//        String newComment = eli.comments;
//        String newDate = eli.date;

        String newPriority = tbo.priority;
        String newName = tbo.title;
        String newComment = tbo.description;
        String newDate = tbo.finish_at;

//        eliPriority;
//        eliName.setText(newName);
//        eliComment.setText(newComment);
//        eliDate.setText(newDate);

        tboName.setText(newName);
        tboComment.setText(newComment);
        tboDate.setText(newDate);

        return view;
    }

    @Override
    public boolean isChildSelectable(int parentPosition, int childPosition) {
        return true;
    }
}
