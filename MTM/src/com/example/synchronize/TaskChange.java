package com.example.synchronize;

import java.io.Serializable;
import java.util.Date;

public class TaskChange implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4503478167348778709L;
	long taskID;
	ActionType action;
	String timestamp;

	public TaskChange(long taskID, ActionType action) {
		this.taskID = taskID;
		this.action = action;
		this.timestamp = new Date().toString();// initialized to current date
												// and time
	}

	public TaskChange(long taskID, String action, String time) {
		this.taskID = taskID;
		setAction(action);
		this.timestamp = time;
	}

	@Override
	public String toString() {
		String change = "";
		change += "Change [ " + timestamp + ", " + taskID + ", " + action
				+ "]\n";
		return change;
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
