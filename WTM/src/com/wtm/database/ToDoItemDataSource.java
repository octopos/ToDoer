package com.wtm.database;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ToDoItemDataSource {

	   private static ToDoItemDataSource instance = null;
	   protected ToDoItemDataSource() {
	      // Exists only to defeat instantiation.
	   }
	  
	  
	  public ToDoItem getItemByItemId(long id) { //id 
		  ToDoItem item = new ToDoItem(); 
		  return item;
	  }
	  

	  public void createItem(String user,String name,String note, String date, String time, long priority) { //change to values
		  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		  	boolean checked = false;
			Key dbKey = KeyFactory.createKey("CDB", "itemDB");
			Entity item = new Entity("items" , dbKey);
			item.setProperty("Username",user);
			item.setProperty("Taskname", name);
			item.setProperty("Note", note);
			item.setProperty("Date", date);
			item.setProperty("Time", time);
			item.setProperty("Priority", priority);
			item.setProperty("Checked", checked );
			datastore.put(item);
			
	  }
	  
	  public void deleteItem(ToDoItem item) {
		  
	  }
	  
	  public void updateItem(ToDoItem item) {
		  
	  }


	  public List<ToDoItem> getToDoListByUId(long userId) {
	    List<ToDoItem> list = new ArrayList<ToDoItem>();
	    return list;
	  }


	  public List<ToDoItem> getInCompleteListByUId(long userId) {
		    List<ToDoItem> list = new ArrayList<ToDoItem>();
		    return list;
		  }
	  
	  public List<ToDoItem> getAllToDo() {
		    List<ToDoItem> list = new ArrayList<ToDoItem>();
		    return list;
		  }
	  public static ToDoItemDataSource getInstance() {
	      if(instance == null) {
	         instance = new ToDoItemDataSource();
	      }
	      return instance;
	   }
	  
}

