package com.wtm.sync;

import java.io.Serializable;
import java.util.Date;

import com.wtm.database.ToDoItem;

@SuppressWarnings("serial")
public class TaskChange implements Serializable {
	long taskID;
	ToDoItem item;
	ActionType action;
	Date timestamp;

	public TaskChange(long taskID, ActionType action) {
		this.taskID = taskID;
		this.action = action;
		this.timestamp = new Date();// initialized to current date
												// and time
	}

	public TaskChange(String action, String time, String taskid, String userid, String name, String note,
			String duetime, String noduetime, String checked, String priority)
	{
		
	}
	
	@SuppressWarnings("deprecation")
	public TaskChange(long taskID, String action, String time) {
		this.taskID = taskID;
		setAction(action);
		this.timestamp = new Date(time);;
	}

	@Override
	public String toString() {
		String change = "";
		change += "Change [ " + timestamp + ", " + taskID + ", " + action
				+ "]\n";
		return change;
	}
	
	public String toSyncString() {
		return timestamp + "\t"+action+"\t";
	}
	
	public long getTaskID() {
		return taskID;
	}

	public void setTaskID(long taskID) {
		this.taskID = taskID;
	}

	public ActionType getAction() {
		return action;
	}

	public void setAction(ActionType action) {
		this.action = action;
	}

	public void setAction(String action) {
		if (action.equals(ActionType.Add.toString()))
			this.action = ActionType.Add;
		else if (action.equals(ActionType.Update.toString()))
			this.action = ActionType.Update;
		else if (action.equals(ActionType.Delete.toString()))
			this.action = ActionType.Delete;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
