package com.changeme.todolist.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changeme.todolist.R;
import com.changeme.todolist.model.ToDoTask;

/**
 * Created by ldc on 2015/6/30.
 */
public class TaskListAdapter extends ArrayAdapter<ToDoTask> {
    private int resourceId;
    private Context context;
    private TaskInfoHolder taskHolder;

    public TaskListAdapter(Context context, int resource, List<ToDoTask> objects) {
        super(context, resource, objects);
        this.resourceId=resource;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout todoView;
        final ToDoTask toDoTask=getItem(position);
        String taskString=toDoTask.getName();
        
        if (convertView==null){
            todoView=new LinearLayout(context);
            String inflater=Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater layoutInflater;
            layoutInflater=(LayoutInflater)getContext().getSystemService(inflater);
            convertView=layoutInflater.inflate(resourceId,todoView,true);
            taskHolder=new TaskInfoHolder();
            taskHolder.isCompletedCb=(CheckBox)todoView.findViewById(R.id.isCompleted);
            taskHolder.taskStatusImg=(ImageView)todoView.findViewById(R.id.taskStatus);
            taskHolder.taskNameTv=(TextView)todoView.findViewById(R.id.taskName);
            taskHolder.taskTimeTv=(TextView)todoView.findViewById(R.id.taskTime);
            convertView.setTag(taskHolder);
        }else {
            taskHolder = (TaskInfoHolder)convertView.getTag();
        }
        
        TextView taskName=taskHolder.getTaskNameTv();
        taskName.setText(taskString);
                
        TextView taskTime=taskHolder.getTaskTimeTv();
        taskTime.setText("asd");
        taskTime.setTextColor(Color.GRAY);
        convertView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					
				}
				return false;
			}
		});
        return convertView;
    }
    
    class TaskInfoHolder {
    	ImageView taskStatusImg;
    	TextView taskNameTv;
    	TextView taskTimeTv;
    	CheckBox isCompletedCb;
		public ImageView getTaskStatusImg() {
			return taskStatusImg;
		}
		public void setTaskStatusImg(ImageView taskStatusImg) {
			this.taskStatusImg = taskStatusImg;
		}
		public TextView getTaskNameTv() {
			return taskNameTv;
		}
		public void setTaskNameTv(TextView taskNameTv) {
			this.taskNameTv = taskNameTv;
		}
		public TextView getTaskTimeTv() {
			return taskTimeTv;
		}
		public void setTaskTimeTv(TextView taskTimeTv) {
			this.taskTimeTv = taskTimeTv;
		}
		public CheckBox getIsCompletedCb() {
			return isCompletedCb;
		}
		public void setIsCompletedCb(CheckBox isCompletedCb) {
			this.isCompletedCb = isCompletedCb;
		}
    	
    }
}
