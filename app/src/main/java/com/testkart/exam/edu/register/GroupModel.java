package com.testkart.exam.edu.register;

/**
 * Created by testkart on 16/5/17.
 */

public class GroupModel {

    private String groupId;
    private String groupName;
    private boolean isGroupChecked;

    public boolean isGroupChecked() {
        return isGroupChecked;
    }

    public void setGroupChecked(boolean groupChecked) {
        isGroupChecked = groupChecked;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
