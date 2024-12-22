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
    private ArrayList<ExpListGroup> filtered;
    private LayoutInflater inflater;

    public ExpandableListAdapter(Context c, ArrayList<ExpListGroup> groups) {
        this.c = c;
        this.filtered = new ArrayList<>();
        this.filtered.addAll(groups);
        this.groups = new ArrayList<>();
        this.groups.addAll(groups);
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return filtered.size();
    }

    @Override
    public int getChildrenCount(int parentPosition) {
        return filtered.get(parentPosition).items.size();
    }

    @Override
    public Object getGroup(int parentPosition) {
        return filtered.get(parentPosition);
    }

    @Override
    public Object getChild(int parentPosition, int childPosition) {
        return filtered.get(parentPosition).items.get(childPosition);
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

        String newTask = elg.elgTaskTypeToShow;
        task.setText(newTask);

        return view;
    }

    @Override
    public View getChildView(int parentPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, null);
        }


        TaskBodyObject tbo = (TaskBodyObject) getChild(parentPosition, childPosition);

        ImageView tboPriority = view.findViewById(R.id.item_priority);
        TextView tboName = view.findViewById(R.id.item_name);
        TextView tboComment = view.findViewById(R.id.item_comment);
        TextView tboDate = view.findViewById(R.id.item_date);

        tboName.setText(tbo.title);
        tboDate.setText(tbo.finish_at);
        tboComment.setText(tbo.description);

        switch (tbo.priority) {
            case  ("high"):
                tboPriority.setImageResource(R.drawable.baseline_circle_24);
                break;
            case ("low"):
                tboPriority.setImageResource(R.drawable.baseline_circle_24_yellow);
                break;
            default:
                tboPriority.setImageResource(R.drawable.ic_house_foreground);
                break;
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int parentPosition, int childPosition) {
        return true;
    }

    public void filterData (String query) {

        query = query.toLowerCase();
        filtered.clear();

        if (query.isEmpty()) {
            filtered.addAll(groups);
        } else {
            for (ExpListGroup elg: groups) {
                ArrayList<TaskBodyObject> childList = elg.items;
                ArrayList<TaskBodyObject> newList = new ArrayList<>();

                for (TaskBodyObject tbo: childList) {
                    if (tbo.title.toLowerCase().contains(query)) {
                        newList.add(tbo);
                    }
                }

                if (!newList.isEmpty()) {
                    String name = elg.elgTaskTypeToShow.substring(0,elg.elgTaskTypeToShow.length()-3);
                    ExpListGroup newExpListGroup = new ExpListGroup(name);
                    newExpListGroup.items = newList;
                    newExpListGroup.elgTaskTypeToShow = name + " (" + newExpListGroup.items.size() + ")";
                    filtered.add(newExpListGroup);
                }
            }
        }
        notifyDataSetChanged();

    }


}
