package com.changeme.todolist.model;

import java.util.List;

/**
 * 一级Item实体类
 */
public class GroupStatusEntity {
    private String groupName;
    /**
     * 二级Item数据列表 *
     */
    private List<ToDoTask> childList;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<ToDoTask> getChildList() {
        return childList;
    }

    public void setChildList(List<ToDoTask> childList) {
        this.childList = childList;
    }

}