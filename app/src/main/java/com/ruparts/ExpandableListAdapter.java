package com.ruparts;

import static com.ruparts.TasksActivity.taskRepository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruparts.context.task.model.TaskFilter;
import com.ruparts.context.task.model.TaskObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        return filtered.get(parentPosition).itemsList.size();
    }

    @Override
    public Object getGroup(int parentPosition) {
        return filtered.get(parentPosition);
    }

    @Override
    public Object getChild(int parentPosition, int childPosition) {
        return filtered.get(parentPosition).itemsList.get(childPosition);
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
            view = inflater.inflate(R.layout.task_explist_group, null);
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
            view = inflater.inflate(R.layout.task_explist_item, null);
        }

        TaskObject task = (TaskObject) getChild(parentPosition, childPosition);

        ImageView taskPriority = view.findViewById(R.id.item_priority);
        TextView tboName = view.findViewById(R.id.item_name);
        TextView tboComment = view.findViewById(R.id.item_comment);
        TextView tboDate = view.findViewById(R.id.item_date);
        TextView notification = view.findViewById(R.id.item_note);

        tboName.setText(task.taskTitle);

        if (task.taskFinishAt != null) {
            Date finishDate = task.taskFinishAt;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
            String formattedDate = simpleDateFormat.format(finishDate);
            tboDate.setText(formattedDate);
        } else {
            tboDate.setText("");
        }

        tboComment.setText(task.taskDescription);

        switch (task.taskPriority) {
            case  ("high"):
                taskPriority.setImageResource(R.drawable.baseline_keyboard_double_arrow_up_24);
                break;
            case ("low"):
                taskPriority.setImageResource(R.drawable.baseline_keyboard_double_arrow_down_24);
                break;
            default:
                taskPriority.setImageResource(R.drawable.equal_priority);
                break;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date fatal = calendar.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, 1);
        Date twoDaysLeft = calendar2.getTime();

        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DATE, 2);
        Date normalDate = calendar3.getTime();

        if (task.taskFinishAt == null) {
            notification.setText("");
            notification.setBackground(c.getDrawable(R.drawable.border_for_notification_task_item_white));
        } else if (task.taskFinishAt != null && task.taskFinishAt.before(fatal)) {
            notification.setText("просрочено");
            notification.setTextColor(c.getColor(R.color.white));
            notification.setBackground(c.getDrawable(R.drawable.border_for_notification_task_item_red));
        } else if (task.taskFinishAt != null && task.taskFinishAt.after(fatal) && task.taskFinishAt.before(twoDaysLeft)) {
            notification.setText("остался 1 день");
            notification.setBackground(c.getDrawable(R.drawable.border_for_notification_task_item_yellow));
        } else if (task.taskFinishAt != null && task.taskFinishAt.after(twoDaysLeft) && task.taskFinishAt.before(normalDate)) {
            notification.setText("осталось 2 дня");
            notification.setBackground(c.getDrawable(R.drawable.border_for_notification_task_item_yellow));
        } else {
            notification.setText("");
            notification.setBackground(c.getDrawable(R.drawable.border_for_notification_task_item_white));
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int parentPosition, int childPosition) {
        return true;
    }

    public void filterData (String query) {

        TaskFilter taskFilter = new TaskFilter();
        taskFilter.search = query;
        List<TaskObject> list = taskRepository.getByFilter(taskFilter);

        for (ExpListGroup elg : TasksActivity.expListContents) {
            ArrayList<TaskObject> newList = new ArrayList<>();

            for (TaskObject task : list) {
                if (task.taskType.equals(elg.elgTaskType)) {
                    newList.add(task);
                }
            }

            if (!newList.isEmpty()) {
                String name = elg.elgTaskTypeToShow.substring(0, elg.elgTaskTypeToShow.length() - 3);
                ExpListGroup newExpListGroup = new ExpListGroup(name);
                newExpListGroup.itemsList = newList;
                newExpListGroup.elgTaskTypeToShow = name + " (" + newExpListGroup.itemsList.size() + ")";
                filtered.add(newExpListGroup);
            }
        }
        notifyDataSetChanged();

    }


}
