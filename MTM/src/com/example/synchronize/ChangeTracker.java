package com.example.synchronize;

import java.util.ArrayList;
import java.util.Date;
import android.content.Context;
import com.example.database.ToDoItem;

public class ChangeTracker {

	private TaskChangesDataSource changesDataStore;
	
	public ChangeTracker(Context context) {
		changesDataStore = new TaskChangesDataSource(context);
	}

	public void createItemChange(ToDoItem item) {
		changesDataStore.createTaskChange(new TaskChange(item.getId(),ActionType.Add));
	}

	public void updateItemChange(ToDoItem item) {
		long taskid = item.getId();
		if(changesDataStore.doesTaskHaveAction(taskid, ActionType.Update))
			changesDataStore.updateTaskChange(taskid,new Date().toString());
		else
			if (!changesDataStore.doesTaskHaveAction(taskid, ActionType.Add))
			changesDataStore.createTaskChange(new TaskChange(item.getId(),ActionType.Update));
	}

	public void deleteItemChange(ToDoItem item) {
		long taskid = item.getId();
		if (changesDataStore.doesTaskHaveAction(taskid, ActionType.Add))
			changesDataStore.deleteChangesByTaskID(taskid);
		else
		{
			changesDataStore.deleteChangesByTaskID(taskid);
			changesDataStore.createTaskChange(new TaskChange(item.getId(),ActionType.Delete));
		}
		
	}

	public void synchronizeChanges() {
		ArrayList<TaskChange> changesList = changesDataStore.getAllTaskChanges();
		for (TaskChange item : changesList) {
			System.out.println(item);
		}
		//clear changes table
		changesDataStore.deleteAllChanges();
	}

}
