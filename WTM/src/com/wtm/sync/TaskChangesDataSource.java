package com.wtm.sync;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.wtm.database.ToDoItem;

public class TaskChangesDataSource {
	DatastoreService datastore;
	Key dbKey;
	public static final String TABLE_TASKCHANGES = "changes";// TC
	public static final String COLUMN_TCTASKID = "taskId";
	public static final String COLUMN_TCACTION = "action";
	public static final String COLUMN_TCTIME = "timestamp";

	public TaskChangesDataSource() {
		datastore = DatastoreServiceFactory.getDatastoreService();
		dbKey = KeyFactory.createKey("CDB", "changesDB");
	}

	public void createTaskChange(TaskChange change) {
		Entity item = new Entity(TABLE_TASKCHANGES, dbKey);
		item.setProperty(COLUMN_TCTASKID, change.getTaskID());
		item.setProperty(COLUMN_TCACTION, change.getAction().toString());
		item.setProperty(COLUMN_TCTIME, change.getTimestampString());
		datastore.put(item);
	}

	
	public boolean doesTaskHaveAction(long taskid, ActionType action) {
		boolean result = false;

		Filter taskFltr = new FilterPredicate(COLUMN_TCTASKID,
				FilterOperator.EQUAL, taskid);
		Query query = new Query(TABLE_TASKCHANGES, dbKey).setFilter(taskFltr);
		Entity target = datastore.prepare(query).asSingleEntity();

		if (target != null) {
			String storedAction = (String) target.getProperty(COLUMN_TCACTION);
			if (storedAction.equals(action.toString()))
				result = true;
		}
		return result;
	}

	public void updateTaskChange(long taskid, Date time) {
		Filter taskFltr = new FilterPredicate(COLUMN_TCTASKID,
				FilterOperator.EQUAL, taskid);
		Query query = new Query(TABLE_TASKCHANGES, dbKey).setFilter(taskFltr);
		Entity target = datastore.prepare(query).asSingleEntity();
		TaskChange ch = new TaskChange(taskid, ActionType.Update);
		ch.setTimestamp(time);
		target.setProperty(COLUMN_TCTIME, ch.getTimestampString());
		datastore.put(target);
	}

	public ArrayList<TaskChange> getAllTaskChanges() {
		ArrayList<TaskChange> allTaskChange = new ArrayList<TaskChange>();
		long taskId;
		String action, time;

		Query query = new Query(TABLE_TASKCHANGES, dbKey);
		List<Entity> items = datastore.prepare(query).asList(
				FetchOptions.Builder.withDefaults());
		if (!items.isEmpty()) {
			for (Entity singleItem : items) {
				taskId = (Long) singleItem.getProperty(COLUMN_TCTASKID);
				action = (String) singleItem.getProperty(COLUMN_TCACTION);
				time = (String) singleItem.getProperty(COLUMN_TCTIME);
				//System.out.printf("Change : %d %s %s\n", taskId, action, time);
				TaskChange tempChange = new TaskChange(taskId, action, time);
				allTaskChange.add(tempChange);
			}
		}

		return allTaskChange;
	}

	public void deleteChangesByTaskID(long taskid) {
		Filter taskFltr = new FilterPredicate(COLUMN_TCTASKID,
				FilterOperator.EQUAL, taskid);
		Query query = new Query(TABLE_TASKCHANGES, dbKey).setFilter(taskFltr);
		Entity target = datastore.prepare(query).asSingleEntity();
		if(target!=null)
		datastore.delete(target.getKey());
	}

	public void deleteAllChanges() {
		Query query = new Query(TABLE_TASKCHANGES, dbKey);
		List<Entity> items = datastore.prepare(query).asList(
				FetchOptions.Builder.withDefaults());
		if (!items.isEmpty())
			for (Entity singleItem : items)
				datastore.delete(singleItem.getKey());
	}

}
