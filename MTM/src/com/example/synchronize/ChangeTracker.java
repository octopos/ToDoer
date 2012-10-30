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

public class ChangeTracker {

	private TaskChangesDataSource changesDataStore;
	private ToDoItemDataSource itemDataStore;

	public ChangeTracker(Context context, ToDoItemDataSource itemDS) {
		changesDataStore = new TaskChangesDataSource(context);
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

	public void synchronizeChanges() {
		ArrayList<TaskChange> changesList = changesDataStore
				.getAllTaskChanges();

		try {

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			String itemString = "";
			ToDoItem changedItem;

			for (TaskChange change : changesList) {
				itemString += change.toSyncString();
				if (change.action != ActionType.Delete)
					itemString += change.getTaskID();
				else {
					changedItem = itemDataStore.getItemByItemId(change
							.getTaskID());
					itemString += changedItem.toSyncString();
				}

				itemString += "\n";

				System.out.println("Writing sync string of : " + change);
				bout.write(itemString.toString().getBytes());
				// System.out.println("Wrote");
			}

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
			// Delete items whose ids are in changes list.
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line;
			StringBuilder sb = new StringBuilder();

			String[] columns;

			while ((line = rd.readLine()) != null) {
				columns = line.split("\t");
				// sb.append(columns.length + " "+line);
				System.out.println(columns.length + " " + line);
				// sb.append(columns[0]+" "+columns[1]+" "+columns[3]);

				// if(columns[0].equals(ActionType.Add.toString()))
				// itemDataStore.createItemWithId(columns[1],columns[2],columns[3],columns[4],columns[5],columns[6],columns[7],
				// columns[8]);
				// else
				// if(columns[0].equals(ActionType.Delete.toString()))
				// itemDataStore.deleteItemById(Long.parseLong(columns[1]));

			}
			rd.close();
			String contentOfMyInputStream = sb.toString();
			System.out.println(contentOfMyInputStream);
			System.out.println("Read response");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// clear changes table
		// changesDataStore.deleteAllChanges();
	}
}
