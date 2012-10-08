package com.example.database;

public class ToDoItem {
	  private long id;
	  private long userId;
	  private String name;
	  private String note;
	  private long dueTime;
	  private boolean checked; 
	  private boolean noDueTime;
	  private long priority;

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
} 
