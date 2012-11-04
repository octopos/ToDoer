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

public class ToDoItemDataSource {

	DatastoreService datastore;
	Key dbKey;

	private static ToDoItemDataSource instance = null;
	
	protected ToDoItemDataSource() {
		datastore = DatastoreServiceFactory.getDatastoreService();
		dbKey = KeyFactory.createKey("CDB", "itemDB");
	}


	public ToDoItem getItemByItemId(long id) { //id 
		ToDoItem item = null;
		List<ToDoItem> list = getAllToDo();
		Iterator<ToDoItem> it = list.iterator();
		while(it.hasNext())
		{
			item = it.next();
			if(item.getId() == id)
			{
				break;
			}
		}
		return item;
	}


	public ToDoItem createItem(String user,String name,String note, String date, long priority) { //change to values
		ToDoItem todo = new ToDoItem();
		todo.setName(name);
		todo.setUserId(getUserID(user));
		todo.setNote(note);
		todo.setDueDate(date);
		todo.setDueTime(dateStrToEpoch(date));
		todo.setPriority(priority);
		todo.setChecked(false);
		boolean checked = false;
		Entity item = new Entity("items" , dbKey);
		item.setProperty("Username",user);
		item.setProperty("Taskname", name);
		item.setProperty("Note", note);
		item.setProperty("Date", date);
		//item.setProperty("Time", time);
		item.setProperty("Priority", priority);
		item.setProperty("Checked", checked );
		datastore.put(item);
		Key id = getItemKey(todo);
		todo.setId(id.getId());
		return todo;
	}

	public void deleteItem(ToDoItem item) {
		datastore.delete(getItemKey(item));
		System.out.println("deleting here");
	}

	public void updateItem(ToDoItem item) {
		Query query = new Query("items", dbKey).addSort("Taskname", Query.SortDirection.DESCENDING);
		List<Entity> itemExists = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
		long id;
		if( !itemExists.isEmpty() )
		{
			for(Entity thisItem : itemExists)
			{
				id = thisItem.getKey().getId();
				if(id == item.getId())
				{	
					System.out.println("id in todo:"+id);
					Users user = new UserDataSource().getUserById(item.getUserId());
					thisItem.setProperty("Username",user.getName());
					thisItem.setProperty("Taskname",item.getName());
					thisItem.setProperty("Note", item.getNote());
					thisItem.setProperty("Date", item.getDueDate());
					//thisItem.setProperty("Time", item.getDueTime2());
					thisItem.setProperty("Priority",item.getPriority());
					thisItem.setProperty("Checked", item.isChecked() );
					datastore.put(thisItem);
				}
			}
		}
	}


	public List<ToDoItem> getToDoListByUId(String username) {
		List<ToDoItem> toDoList = new ArrayList<ToDoItem>();
		Filter existFltr = new FilterPredicate("Username" , FilterOperator.EQUAL , username );
		Query query = new Query("items" , dbKey).setFilter(existFltr);
		List<Entity> items = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
		long uid = getUserID(username);
		System.out.println(items.size());
		if (!items.isEmpty()) {
			//int count = 1; 
			for (Entity singleItem : items) {
				ToDoItem temp = new ToDoItem();
				temp.setId(singleItem.getKey().getId());
				System.out.println("id:"+singleItem.getKey().getId());
				temp.setUserId(uid);
				temp.setName((String)singleItem.getProperty("Taskname"));
				//temp.setName("abcd");
				temp.setNote((String)singleItem.getProperty("Note"));
				//Remove this
				temp.setDueDate((String)singleItem.getProperty("Date"));
				String date = (String)singleItem.getProperty("Date");
				if(!date.isEmpty())
				{
					temp.setDueTime(this.dateStrToEpoch((String)singleItem.getProperty("Date")));
					System.out.print(temp.getDueTime());
				}
				//temp.setDueTime2((String)singleItem.getProperty("Time"));
				//End Remove
				temp.setChecked((Boolean) singleItem.getProperty("Checked"));
				temp.setPriority((Long)singleItem.getProperty("Priority"));
				toDoList.add(temp);
			}
		}
		return toDoList;
	}


	public List<ToDoItem> getInCompleteListByUId(String username) {

		List<ToDoItem> list = new ArrayList<ToDoItem>();
		List<ToDoItem> workingset = getToDoListByUId(username);
		Iterator<ToDoItem> it = workingset.iterator();
		ToDoItem temp;
		while(it.hasNext())
		{
			temp = it.next();
			if(!temp.isChecked())
			{
				list.add(temp);
			}
		}
		return list;
	}

	public List<ToDoItem> getAllToDo() {
		List<ToDoItem> list = new ArrayList<ToDoItem>();
		Query query = new Query("items", dbKey).addSort("Taskname", Query.SortDirection.DESCENDING);
		List<Entity> items = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
		System.out.println(items.size());
		if (!items.isEmpty()) {
			for (Entity singleItem : items) {
				ToDoItem temp = new ToDoItem();
				temp.setId(singleItem.getKey().getId());
				long uid = getUserID((String)singleItem.getProperty("Username"));
				temp.setUserId(uid);
				temp.setName((String)singleItem.getProperty("Taskname"));
				//temp.setName("abcd");
				temp.setNote((String)singleItem.getProperty("Note"));
				//Remove this
				temp.setDueDate((String)singleItem.getProperty("Date"));
				System.out.println((String)singleItem.getProperty("Date"));
				String date = (String)singleItem.getProperty("Date"); 
				if(!date.isEmpty())
					{
						temp.setDueTime(this.dateStrToEpoch((String)singleItem.getProperty("Date")));
						System.out.print(temp.getDueTime());
					}
				//temp.setDueTime2((String)singleItem.getProperty("Time"));
				//System.out.println((String)singleItem.getProperty("Time"));
				//End Remove
				temp.setChecked((Boolean) singleItem.getProperty("Checked"));
				temp.setPriority((Long)singleItem.getProperty("Priority"));
				list.add(temp);
			}
		}
		return list;
	}
	//extra functions
	public long dateStrToEpoch(String dateStr)
	{
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date date = null;
		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long epoch = date.getTime();
		return epoch;
	}
	public void checkIt(long id,boolean check) //calls the update with the value of check
	{
		ToDoItem item = getItemByItemId(id);
		item.setChecked(check);
		this.updateItem(item);

	}
	public static Key getItemKey(ToDoItem item){
		long id = 0;
		Key itemid = null; 
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key dbKey = KeyFactory.createKey("CDB", "itemDB");
		Filter existFltr = new FilterPredicate("Taskname" , FilterOperator.EQUAL , item.getName() );
		Query query = new Query("items" , dbKey).setFilter(existFltr);
		List<Entity> itemExists = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
		if( !itemExists.isEmpty() )
		{
			for( Entity thisItem: itemExists ){
				id = thisItem.getKey().getId();
				if(id == item.getId())
					itemid = thisItem.getKey();
			}
		}
		return itemid;
	}
	public static long getUserID(String username){
		long userid = 0;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key dbKey = KeyFactory.createKey("CDB", "userDB");
		Filter existFltr = new FilterPredicate("Username" , FilterOperator.EQUAL , username );
		Query query = new Query("users" , dbKey).setFilter(existFltr);
		List<Entity> usersExist = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
		if( !usersExist.isEmpty() )
		{
			for( Entity thisUser : usersExist ){
				userid = thisUser.getKey().getId();
			}
		}
		return userid;
	}
	public Date epochToDate(long millis){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		Date date = cal.getTime();
		//System.out.println("Date:"+cal.getTime());
		return date;
	}
	
	public String dateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		//System.out.println("Date format:" + sdf.format(date));
		return sdf.format(date);
	} 
	
	public static ToDoItemDataSource getInstance() {
		if(instance == null) {
			instance = new ToDoItemDataSource();
		}
		return instance;
	}

}

