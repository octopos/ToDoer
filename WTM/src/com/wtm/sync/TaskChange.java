package com.wtm.sync;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

	public TaskChange(String time, String action, String taskid,long userid, String name,
			String note, String duetime, String noduetime, String checked,
			String priority) {
		taskID = Long.parseLong(taskid);
		setAction(action);
		setTimestamp(time);
		item = new ToDoItem();
		item.setId(taskID);
		item.setUserId(userid);
		item.setName(name);
		item.setNote(note);
		if (noduetime.equals("true")) {
			item.setDueTime(0);
			item.setNoDueTime(true);
		}
		else{
			item.setDueTime(Long.parseLong(duetime));
			item.setNoDueTime(false);
		}
		if (checked.equals("true"))
			item.setChecked(true);
		else
			item.setChecked(false);
		item.setPriority(priority);
	}

	public TaskChange(String time, String action, String taskid) {
		taskID = Long.parseLong(taskid);
		setAction(action);
		setTimestamp(time);
	}

	public TaskChange(long taskID, String action, String time) {
		this.taskID = taskID;
		setAction(action);
		setTimestamp(time);
	}

	public String getTimestampString() {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(timestamp);
	}

	public void setTimestamp(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			this.timestamp = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		String change = "";
		change += "Change [ " + getTimestampString() + ", " + taskID + ", "
				+ action + "]\n";
		return change;
	}

	public String toSyncString() {
		return getTimestampString() + "\t" + action + "\t";
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

	public ToDoItem getItem() {
		return item;
	}

}
