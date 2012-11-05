package com.example.synchronize;

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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import com.example.database.ToDoItem;
import com.example.database.ToDoItemDataSource;
import com.example.database.UserDataSource;
import com.example.database.Users;

public class ChangeTracker {

	private TaskChangesDataSource changesDataStore;
	private ToDoItemDataSource itemDataStore;
	private UserDataSource userDataStore;

	public ChangeTracker(Context context, ToDoItemDataSource itemDS) {
		changesDataStore = new TaskChangesDataSource(context);
		userDataStore = new UserDataSource(context);
		itemDataStore = itemDS;
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

	public void synchronizeChanges(long userid) {
		//changesDataStore.deleteAllChanges();
		
		ArrayList<TaskChange> changesList = changesDataStore
				.getAllTaskChanges();

		try {

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			String sendString = "", itemString = "";
			ArrayList<Long> changedIds = new ArrayList<Long>();

			Users targetUser = userDataStore.getUserById(userid);
			sendString += targetUser.toSyncString();

			ToDoItem changedItem;
			for (TaskChange change : changesList) {
				if (change.action == ActionType.Delete) {
					itemString = change.getTaskID() + "";
					System.out.println("Writing sync string of : " + change);
					sendString += change.toSyncString() + itemString + "\n";
				} else {
					changedItem = itemDataStore.getItemByItemId(change
							.getTaskID());
					itemString = changedItem.toSyncString();
					if (changedItem.getUserId() == userid) {
						System.out
								.println("Writing sync string of : " + change);
						sendString += change.toSyncString() + itemString + "\n";
						changedIds.add(changedItem.getId());
					}
				}
			}
			bout.write(sendString.toString().getBytes());
			System.out.println("Wrote:\n" + sendString);

			if (bout.size() != 0) {
				byte[] bytes = bout.toByteArray();
				// System.out.println("Got bytes");

				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://codecious.appspot.com/sync.do");
				// System.out.println("Initialized ");
				InputStream in = new ByteArrayInputStream(bytes);
				httppost.setEntity(new InputStreamEntity(in, bytes.length));
				// System.out.println("Sending request");
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				System.out.println("Got response " + response.toString());

				for (Long oneID : changedIds) {
					itemDataStore.deleteItemById(oneID);
				}

				BufferedReader requestReader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				String line;
				String[] columns;
				// Update user name
				line = requestReader.readLine();// User line
				columns = line.split("\t");
				if (!targetUser.getPwd().equals(columns[1])) {
					targetUser.setPwd(columns[1]);
					userDataStore.updateUsers(targetUser);
				}

				while ((line = requestReader.readLine()) != null) {
					columns = line.split("\t");
					System.out.println(columns.length + " " + line);

					if (columns[0].equals(ActionType.Add.toString())) {
						// id+"\t"+name+"\t"+note+"\t"+dueTime+"\t"+noDueTime+"\t"+checked+"\t"+priority;
						itemDataStore.createItemWithId(targetUser.getId(),
								columns[1], columns[2], columns[3], columns[4],
								columns[5], columns[6], columns[7]);
					} else if (columns[0].equals(ActionType.Delete.toString()))
						itemDataStore
								.deleteItemById(Long.parseLong(columns[1]));
				}

				for (Long oneId : changedIds)
					changesDataStore.deleteChangesByTaskID(oneId);
				changesDataStore.removeDeleteChanges();
				changesDataStore.deleteAllChanges();

				requestReader.close();
				System.out.println("Read response");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void synchronizeUsers() {
		ArrayList<Users> usersList = (ArrayList<Users>) userDataStore
				.getAllUsers();
		ArrayList<String> existingUsers = new ArrayList<String>();

		try {

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			String sendString = "Get Users\n";
			for (Users user : usersList) {
				//System.out.println("Writing sync string of : " + user);
				//sendString += user.toSyncString();
				existingUsers.add(user.getName());
			}

			bout.write(sendString.toString().getBytes());
			System.out.println("Wrote:\n" + sendString);

			if (bout.size() != 0) {
				byte[] bytes = bout.toByteArray();
				// System.out.println("Got bytes");

				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://codecious.appspot.com/sync.do");
				// System.out.println("Initialized ");
				InputStream in = new ByteArrayInputStream(bytes);
				httppost.setEntity(new InputStreamEntity(in, bytes.length));
				// System.out.println("Sending request");
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				System.out.println("Got response " + response.toString());

				BufferedReader requestReader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				String line;
				String[] columns;
				while ((line = requestReader.readLine()) != null) {
					columns = line.split("\t");
					System.out.println("user : "+line);
					Users targetUser = new Users();
					targetUser.setName(columns[0]);
					targetUser.setPwd(columns[1]);
					if (existingUsers.contains(targetUser.getName())) {
						userDataStore.updateUsers(targetUser);
					} else
						userDataStore.createUsers(targetUser.getName(),
								targetUser.getPwd());
				}
				requestReader.close();
				System.out.println("Read response");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// clear changes table

	}
}
