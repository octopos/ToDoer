package com.wtm.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.wtm.sync.ChangeTracker;
import com.wtm.todo.DueDateComparator;

public class ToDoItemDataSource {

	DatastoreService datastore;
	Key dbKey;

	ChangeTracker tracker;

	private static ToDoItemDataSource instance = null;

	public ToDoItemDataSource() {
		datastore = DatastoreServiceFactory.getDatastoreService();
		dbKey = KeyFactory.createKey("CDB", "itemDB");
		tracker = new ChangeTracker(this);
	}

	public ToDoItem createItem(String username, String name, String note,
			String date, long priority) { // change to values
		ToDoItem todo = new ToDoItem();
		todo.setName(name);
		todo.setUserId(UserDataSource.getUserID(username));
		todo.setNote(note);
		todo.setDueTime(date);// date's format is "MM/dd/yyyy"
		todo.setPriority(priority);
		todo.setChecked(false);
		boolean checked = false;
		Entity item = new Entity("items", dbKey);
		item.setProperty("Username", username);
		item.setProperty("Taskname", name);
		item.setProperty("Note", note);
		item.setProperty("Date", date);// date's format is "MM/dd/yyyy"
		item.setProperty("Priority", priority);
		item.setProperty("Checked", checked);
		datastore.put(item);
		Key id = getItemKey(todo);
		todo.setId(id.getId());
		tracker.taskCreatedChange(todo);
		return todo;
	}

	public ToDoItem createItem(String username, ToDoItem todo) {
		Entity item = new Entity("items", dbKey);
		item.setProperty("Username", username);
		item.setProperty("Taskname", todo.getName());
		item.setProperty("Note", todo.getNote());
		item.setProperty("Date", todo.getDueDate());// date's format is "MM/dd/yyyy"
		item.setProperty("Priority", todo.getPriority());
		item.setProperty("Checked", todo.isChecked());
		datastore.put(item);
		Key id = getItemKey(todo);
		todo.setId(id.getId());
		return todo;
	}
	
	public void updateItem(ToDoItem item, Boolean... trackingOption) {
		boolean track = trackingOption.length > 0 ? trackingOption[0].booleanValue() : true;
		Query query = new Query("items", dbKey).addSort("Taskname",
				Query.SortDirection.DESCENDING);
		List<Entity> itemExists = datastore.prepare(query).asList(
				FetchOptions.Builder.withDefaults());
		long id;
		if (!itemExists.isEmpty()) {
			for (Entity thisItem : itemExists) {
				id = thisItem.getKey().getId();
				if (id == item.getId()) {
					//System.out.println("id in todo:" + id);
					Users user = new UserDataSource().getUserById(item
							.getUserId());
					thisItem.setProperty("Username", user.getName());
					thisItem.setProperty("Taskname", item.getName());
					thisItem.setProperty("Note", item.getNote());
					thisItem.setProperty("Date", item.getDueDate());
					thisItem.setProperty("Priority", item.getPriority());
					thisItem.setProperty("Checked", item.isChecked());
					datastore.put(thisItem);
					if(track)
					tracker.taskupdatedChange(item);
				}
			}
		}
	}

	public void checkIt(long id, boolean check) // calls the update with the
												// value of check
	{
		ToDoItem item = getItemByItemId(id);
		item.setChecked(check);
		this.updateItem(item);

	}

	public void deleteItem(ToDoItem item) {
		datastore.delete(getItemKey(item));
		tracker.taskDeletedChange(item);
		// System.out.println("deleting here");
	}

	public void deleteItemById(long taskID) {//Used by sync method so no need to track changes made by this
		ToDoItem target = getItemByItemId(taskID);
		if(target!=null)
			datastore.delete(getItemKey(target));
	}
	
	public List<ToDoItem> getAllToDo() {
		List<ToDoItem> list = new ArrayList<ToDoItem>();
		Query query = new Query("items", dbKey).addSort("Taskname",
				Query.SortDirection.DESCENDING);
		List<Entity> items = datastore.prepare(query).asList(
				FetchOptions.Builder.withDefaults());
		//System.out.println(items.size());
		if (!items.isEmpty()) {
			for (Entity singleItem : items) {
				ToDoItem temp = new ToDoItem();
				temp.setId(singleItem.getKey().getId());
				long uid = UserDataSource.getUserID((String) singleItem
						.getProperty("Username"));
				temp.setUserId(uid);
				temp.setName((String) singleItem.getProperty("Taskname"));
				temp.setNote((String) singleItem.getProperty("Note"));
				temp.setDueTime((String) singleItem.getProperty("Date"));
				temp.setChecked((Boolean) singleItem.getProperty("Checked"));
				temp.setPriority((Long) singleItem.getProperty("Priority"));
				list.add(temp);
			}
		}
		return list;
	}

	public ToDoItem getItemByItemId(long id) { // id
		ToDoItem item = null;
		List<ToDoItem> list = getAllToDo();
		Iterator<ToDoItem> it = list.iterator();
		while (it.hasNext()) {
			item = it.next();
			if (item.getId() == id) {
				break;
			}
		}
		return item;
	}

	public List<ToDoItem> getToDoListByUsername(String username) {
		List<ToDoItem> toDoList = new ArrayList<ToDoItem>();
		Filter existFltr = new FilterPredicate("Username",
				FilterOperator.EQUAL, username);
		Query query = new Query("items", dbKey).setFilter(existFltr);
		List<Entity> items = datastore.prepare(query).asList(
				FetchOptions.Builder.withDefaults());
		long uid = UserDataSource.getUserID(username);
		//System.out.println(items.size());
		if (!items.isEmpty()) {
			for (Entity singleItem : items) {
				ToDoItem temp = new ToDoItem();
				temp.setId(singleItem.getKey().getId());
				System.out.println("id:" + singleItem.getKey().getId());
				temp.setUserId(uid);
				temp.setName((String) singleItem.getProperty("Taskname"));
				temp.setNote((String) singleItem.getProperty("Note"));
				temp.setDueTime((String) singleItem.getProperty("Date"));
				temp.setChecked((Boolean) singleItem.getProperty("Checked"));
				temp.setPriority((Long) singleItem.getProperty("Priority"));
				toDoList.add(temp);
			}
		}
		return toDoList;
	}

	public List<ToDoItem> getInCompleteListByUId(String username) {
		List<ToDoItem> list = new ArrayList<ToDoItem>();
		List<ToDoItem> workingset = getToDoListByUsername(username);
		Iterator<ToDoItem> it = workingset.iterator();
		ToDoItem temp;
		while (it.hasNext()) {
			temp = it.next();
			if (!temp.isChecked()) {
				list.add(temp);
			}
		}
		return list;
	}

	public static Key getItemKey(ToDoItem item) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key dbKey = KeyFactory.createKey("CDB", "itemDB");
		Filter existFltr = new FilterPredicate("Taskname",
				FilterOperator.EQUAL, item.getName());
		Query query = new Query("items", dbKey).setFilter(existFltr);
		Entity target = datastore.prepare(query).asSingleEntity();
		return target.getKey();
	}

	
	public static ToDoItemDataSource getInstance() {
		if (instance == null) {
			instance = new ToDoItemDataSource();
		}
		return instance;
	}

	public ChangeTracker getTracker() {
		return tracker;
	}

	

	
}
