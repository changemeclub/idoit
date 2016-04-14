package com.changeme.todolist;

import com.changeme.todolist.model.ToDoTask;

public interface TaskCompleteListener {
	public void onTaskStatusChange(ToDoTask toDoTask);
}
