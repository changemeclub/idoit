package com.changeme.todolist.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.changeme.todolist.R;
import com.changeme.todolist.model.GroupStatusEntity;
import com.changeme.todolist.model.ToDoTask;

public class TaskListGroupAdapter extends BaseExpandableListAdapter{
	 private LayoutInflater inflater = null;
	    private List<GroupStatusEntity> groupList;

	    /**
	     * 构造方法
	     *
	     * @param context
	     * @param oneList
	     */
	    public TaskListGroupAdapter(Context context, List<GroupStatusEntity> group_list) {
	        this.groupList = group_list;
	        inflater = (LayoutInflater) context
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }

	    /**
	     * 返回一级Item总数
	     */
	    @Override
	    public int getGroupCount() {
	        // TODO Auto-generated method stub
	        return groupList.size();
	    }

	    /**
	     * 返回二级Item总数
	     */
	    @Override
	    public int getChildrenCount(int groupPosition) {
	        if (groupList.get(groupPosition).getChildList() == null) {
	            return 0;
	        } else {
	            return groupList.get(groupPosition).getChildList().size();
	        }
	    }

	    /**
	     * 获取一级Item内容
	     */
	    @Override
	    public Object getGroup(int groupPosition) {
	        return groupList.get(groupPosition);
	    }

	    /**
	     * 获取二级Item内容
	     */
	    @Override
	    public Object getChild(int groupPosition, int childPosition) {
	        return groupList.get(groupPosition).getChildList().get(childPosition);
	    }

	    @Override
	    public long getGroupId(int groupPosition) {
	        return groupPosition;
	    }

	    @Override
	    public long getChildId(int groupPosition, int childPosition) {
	        return childPosition;
	    }

	    @Override
	    public boolean hasStableIds() {
	        // TODO Auto-generated method stub
	        return false;
	    }

	    @Override
	    public View getGroupView(int groupPosition, boolean isExpanded,
	                             View convertView, ViewGroup parent) {

	        GroupViewHolder holder = new GroupViewHolder();

	        if (convertView == null) {
	            convertView = inflater.inflate(R.layout.group_status_item, null);
	        }
	        holder.groupName = (TextView) convertView
	                .findViewById(R.id.one_status_name);

	        holder.groupName.setText(groupList.get(groupPosition).getGroupName());

	        return convertView;
	    }

	    @Override
	    public View getChildView(int groupPosition, int childPosition,
	                             boolean isLastChild, View convertView, ViewGroup parent) {
	        ChildViewHolder viewHolder = null;
	        ToDoTask entity = (ToDoTask) getChild(groupPosition,
	                childPosition);
	        if (convertView != null) {
	            viewHolder = (ChildViewHolder) convertView.getTag();
	        } else {
	            viewHolder = new ChildViewHolder();
	            convertView = inflater.inflate(R.layout.task_list_item, null);
	            viewHolder.isCompletedCb = (CheckBox) convertView
	                    .findViewById(R.id.isCompleted);
	            viewHolder.taskNameTv=(TextView)convertView.findViewById(R.id.taskName);
	            viewHolder.taskTimeTv=(TextView)convertView.findViewById(R.id.taskTime);
	            viewHolder.taskStatusImg=(ImageView)convertView.findViewById(R.id.taskStatus);
	            convertView.setTag(viewHolder);
	        }
	        
	        TextView taskName=viewHolder.getTaskNameTv();
	        taskName.setText(entity.getName());
	                
	        TextView taskTime=viewHolder.getTaskTimeTv();
	        taskTime.setText("十分钟后");
	        taskTime.setTextColor(Color.GRAY);
	        return convertView;
	    }

	    @Override
	    public boolean isChildSelectable(int groupPosition, int childPosition) {
	        return true;
	    }

	    private class GroupViewHolder {
	        TextView groupName;
	    }

	    private class ChildViewHolder {
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
