package com.example.synchronize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class ChangeTracker {

	private TaskChangesDataSource changesDataStore;

	public ChangeTracker(Context context) {
		changesDataStore = new TaskChangesDataSource(context);
	}

	public void createItemChange(ToDoItem item) {
		changesDataStore.createTaskChange(new TaskChange(item.getId(),
				ActionType.Add));
	}

	public void updateItemChange(ToDoItem item) {
		long taskid = item.getId();
		if (changesDataStore.doesTaskHaveAction(taskid, ActionType.Update))
			changesDataStore.updateTaskChange(taskid, new Date().toString());
		else if (!changesDataStore.doesTaskHaveAction(taskid, ActionType.Add))
			changesDataStore.createTaskChange(new TaskChange(item.getId(),
					ActionType.Update));
	}

	public void deleteItemChange(ToDoItem item) {
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
			ObjectOutputStream oostream = new ObjectOutputStream(bout);

			for (TaskChange item : changesList) {
				System.out.println(item);
				oostream.writeChars(item.toString());
			}
			oostream.close();
			byte[] bytes = bout.toByteArray();

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://www.yoursite.com/script.php");

			InputStream in = new ByteArrayInputStream(bytes);
			httppost.setEntity(new InputStreamEntity(in, bytes.length));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			
			HttpEntity outEntity = response.getEntity();
			outEntity.writeTo(bout);
			System.out.println(bout.toString());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			System.out.println("Client protocol exception");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IO exception");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("IO exception");
		}

		// clear changes table
		// changesDataStore.deleteAllChanges();
	}

}
