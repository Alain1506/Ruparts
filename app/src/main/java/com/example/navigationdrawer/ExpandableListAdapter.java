package com.example.navigationdrawer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.navigationdrawer.helperclasses.TaskBodyObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    @SuppressLint("ResourceAsColor")
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
        TextView notification = view.findViewById(R.id.item_note);

        tboName.setText(tbo.title);

        if (tbo.finish_at != null) {
            Date finishDate = tbo.finish_at;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
            String formattedDate = simpleDateFormat.format(finishDate);
            tboDate.setText(formattedDate);
        } else {
            tboDate.setText("");
        }

        tboComment.setText(tbo.description);

        switch (tbo.priority) {
            case  ("high"):
                tboPriority.setImageResource(R.drawable.baseline_keyboard_double_arrow_up_24);
                break;
            case ("low"):
                tboPriority.setImageResource(R.drawable.baseline_keyboard_double_arrow_down_24);
                break;
            default:
                tboPriority.setImageResource(R.drawable.equal_priority);
                break;
        }

//        changeableDate.set(Calendar.YEAR, year);
//        changeableDate.set(Calendar.MONTH, monthOfYear);
//        changeableDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        Calendar calendar = Calendar.getInstance();
        Date fatal = calendar.getTime();

//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.add(Calendar.DATE, 1);
//        Date tomorrow = calendar1.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, 2);
        Date twoDaysElse = calendar2.getTime();

        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DATE, 3);
        Date normalDate = calendar3.getTime();


        if (tbo.finish_at != null && tbo.finish_at.before(fatal)) {
            notification.setText("просрочено");
            notification.setBackground(c.getDrawable(R.drawable.border_for_notification_task_item_red));
        } else if (tbo.finish_at != null && tbo.finish_at.after(fatal) && tbo.finish_at.before(twoDaysElse)) {
            notification.setText("остался 1 день");
            notification.setBackground(c.getDrawable(R.drawable.border_for_notification_task_item_yellow));
        } else if (tbo.finish_at != null && tbo.finish_at.after(twoDaysElse) && tbo.finish_at.before(normalDate)) {
            notification.setText("осталось 2 дня");
            notification.setBackground(c.getDrawable(R.drawable.border_for_notification_task_item_yellow));
        } else {
            notification.setText("");
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
