package com.wtm.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToDoItem {
	private long id;
	private long userId;
	private String name;
	private String note;
	private long dueTime;
	private boolean checked;
	private boolean noDueTime;
	private long priority;
	
	// private String dueTime2;
	public String getDueDate() {
		if(dueTime==0)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(dueTime);
		Date date = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		// System.out.println("Date format:" + sdf.format(date));
		return sdf.format(date);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return name;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public long getDueTime() {
		return dueTime;
	}

	public void setDueTime(long dueTime) {
		this.dueTime = dueTime;
	}

	public void setDueTime(String dateStr) {
		long epoch = 0;
		if (!dateStr.isEmpty()) {
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			Date date = null;
			try {
				date = df.parse(dateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			epoch = date.getTime();
		}
		this.dueTime = epoch;
		if (epoch == 0)
			setNoDueTime(true);
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isNoDueTime() {
		return noDueTime;
	}

	public void setNoDueTime(boolean noDueTime) {
		this.noDueTime = noDueTime;
	}

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}
	
	public void setPriority(String priority) {
		this.priority = Long.parseLong(priority);
	}

	public String toSyncString() {
		return id + "\t" + name + "\t" + note + "\t" + dueTime + "\t"
				+ noDueTime + "\t" + checked + "\t" + priority + "\n";
	}

}
