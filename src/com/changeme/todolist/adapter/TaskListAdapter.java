package com.changeme.todolist.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changeme.todolist.R;
import com.changeme.todolist.TaskCompleteListener;
import com.changeme.todolist.model.ToDoTask;

/**
 * Created by ldc on 2015/6/30.
 */
public class TaskListAdapter extends ArrayAdapter<ToDoTask> {
    private int resourceId;
    private Context context;
//    private TaskInfoHolder taskHolder;
    private TaskCompleteListener taskCompleteListener;

	public TaskListAdapter(Context context, int resource, List<ToDoTask> objects) {
        super(context, resource, objects);
        this.resourceId=resource;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ToDoTask toDoTask=getItem(position);
        String taskString=toDoTask.getName();
        final TaskInfoHolder taskHolder;
        if (convertView==null){
            String inflater=Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater layoutInflater;
            layoutInflater=(LayoutInflater)getContext().getSystemService(inflater);
            convertView=layoutInflater.inflate(resourceId,null);
            taskHolder=new TaskInfoHolder();
            taskHolder.isCompletedCb=(CheckBox)convertView.findViewById(R.id.isCompleted);
            taskHolder.taskStatusImg=(ImageView)convertView.findViewById(R.id.taskStatus);
            taskHolder.taskNameTv=(TextView)convertView.findViewById(R.id.taskName);
            taskHolder.taskTimeTv=(TextView)convertView.findViewById(R.id.taskTime);
            convertView.setTag(taskHolder);
        }else {
            taskHolder = (TaskInfoHolder)convertView.getTag();
        }
        
        TextView taskName=taskHolder.getTaskNameTv();
        taskName.setText(taskString);
                
        TextView taskTime=taskHolder.getTaskTimeTv();
        taskTime.setText("asd");
        taskTime.setTextColor(Color.GRAY);
        
        CheckBox isCompletedCb=taskHolder.getIsCompletedCb();
        isCompletedCb.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if(((CheckBox)view).isChecked()){
					toggleItemStatus(true,taskHolder.getTaskNameTv());
					toDoTask.setCompleted(ToDoTask.IS_COMPLETE);
					taskCompleteListener.onTaskStatusChange(toDoTask);
				}else{
					toggleItemStatus(false,taskHolder.getTaskNameTv());
					toDoTask.setCompleted(ToDoTask.IS_NOT_COMPLETE);
					taskCompleteListener.onTaskStatusChange(toDoTask);
				}
			}
		});
        return convertView;
    }
    
    /**
     * toggle 所选择任务的显示效果
     */
    private void toggleItemStatus(Boolean checked,TextView itemView){
    	if(checked){
    		itemView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    		itemView.setTextColor(Color.LTGRAY);
    	}else{
    		itemView.getPaint().setStrikeThruText(false);
    		itemView.setTextColor(Color.BLACK);
    	}
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
    
    public TaskCompleteListener getTaskCompleteListener() {
		return taskCompleteListener;
	}

	public void setTaskCompleteListener(TaskCompleteListener taskCompleteListener) {
		this.taskCompleteListener = taskCompleteListener;
	}

}
