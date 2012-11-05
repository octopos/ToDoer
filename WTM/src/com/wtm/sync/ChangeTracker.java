package com.wtm.sync;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

import com.wtm.database.ToDoItem;
import com.wtm.database.ToDoItemDataSource;
import com.wtm.database.UserDataSource;
import com.wtm.database.Users;

public class ChangeTracker {

	private TaskChangesDataSource changesDataStore;
	private ToDoItemDataSource itemDataStore;

	public ChangeTracker(ToDoItemDataSource parent) {
		changesDataStore = new TaskChangesDataSource();
		itemDataStore = parent;
	}

	public void taskCreatedChange(ToDoItem item) {
		changesDataStore.createTaskChange(new TaskChange(item.getId(),
				ActionType.Add));
	}

	public void taskupdatedChange(ToDoItem item) {
		long taskid = item.getId();
		if (changesDataStore.doesTaskHaveAction(taskid, ActionType.Update))
			changesDataStore.updateTaskChange(taskid, new Date());
		else if (!changesDataStore.doesTaskHaveAction(taskid, ActionType.Add))
			changesDataStore.createTaskChange(new TaskChange(item.getId(),
					ActionType.Update));
	}

	public void taskDeletedChange(ToDoItem item) {
		long taskid = item.getId();
		if (changesDataStore.doesTaskHaveAction(taskid, ActionType.Add))
			changesDataStore.deleteChangesByTaskID(taskid);
		else {
			changesDataStore.deleteChangesByTaskID(taskid);
			changesDataStore.createTaskChange(new TaskChange(item.getId(),
					ActionType.Delete));
		}

	}

	
	public ArrayList<TaskChange> getTaskChangesByUsername(String username) {
		System.out.println("Getting changes for username : " + username);
		long targetId = UserDataSource.getUserID(username);
		ArrayList<TaskChange> allTaskChange = changesDataStore
				.getAllTaskChanges();
		ArrayList<TaskChange> changesList = new ArrayList<TaskChange>();
		ToDoItem changedItem;
		for (TaskChange change : allTaskChange) {
			System.out.println("Processing change " + change.toSyncString());
			if (change.action != ActionType.Delete) {
				changedItem = itemDataStore.getItemByItemId(change.getTaskID());
				System.out.println("Processing item "
						+ changedItem.toSyncString());
				if (targetId == changedItem.getUserId()) {
					System.out.println("Adding");
					changesList.add(change);
				}
			}
		}
		return changesList;
	}

	public void deleteChangesByUsername(String username) {
		System.out.println("Deleting changes for username : " + username);
		long targetId = UserDataSource.getUserID(username);
		ArrayList<TaskChange> allTaskChange = changesDataStore
				.getAllTaskChanges();
		ToDoItem changedItem;
		for (TaskChange change : allTaskChange) {
			System.out.println("Deleting change " + change.toSyncString());
			if (change.action == ActionType.Delete) {
				changesDataStore.deleteChangesByTaskID(change.taskID);
			}
			else{
				changedItem = itemDataStore.getItemByItemId(change.getTaskID());
				System.out.println("Processing item "
						+ changedItem.toSyncString());
				if (targetId == changedItem.getUserId()) {
					System.out.println("Deleting");
					changesDataStore.deleteChangesByTaskID(change.taskID);
				}
			}
		}
	}
	
	public ArrayList<TaskChange> getTaskDeletes() {
		ArrayList<TaskChange> allTaskChange = changesDataStore
				.getAllTaskChanges();
		ArrayList<TaskChange> changesList = new ArrayList<TaskChange>();
		for (TaskChange change : allTaskChange)
			if (change.action == ActionType.Delete)
				changesList.add(change);

		return changesList;
	}

	public String getTestingChanges(long userid) {
		return "test\ttestt\n";
		/*
		String sendString = "", itemString = "";

		UserDataSource userDataStore = new UserDataSource();
		Users targetUser = userDataStore.getUserById(userid);
		sendString += targetUser.toSyncString();

		ToDoItem changedItem;
		ArrayList<TaskChange> changesList = changesDataStore
				.getAllTaskChanges();
		for (TaskChange change : changesList) {
			if (change.action == ActionType.Delete) {
				itemString = change.getTaskID() + "";
				//System.out.println("Writing sync string of : " + change);
				sendString += change.toSyncString() + itemString + "\n";
			} else {
				changedItem = itemDataStore.getItemByItemId(change.getTaskID());
				if (changedItem.getUserId() == userid) {
					// System.out.println("Writing sync string of : " + change);
					changedItem.setName(changedItem.getName()+" test");
					itemString = changedItem.toSyncString();
					sendString += change.toSyncString() + itemString;
				}
			}
		}
		System.out.println("Sending : " + sendString);
		//deleteChangesByUsername("abc");
		return sendString;
		*/
	}

}
