package com.wtm.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	   private static ToDoItemDataSource instance = null;
	   protected ToDoItemDataSource() {
	      // Exists only to defeat instantiation.
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
		  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Key dbKey = KeyFactory.createKey("CDB", "itemDB");
			datastore.delete(getItemID(item));
	  }
	  
	  public void updateItem(ToDoItem item) {
		  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Key dbKey = KeyFactory.createKey("CDB", "itemDB");
			Query query = new Query("items", dbKey).addSort("Taskname", Query.SortDirection.DESCENDING);
			List<Entity> itemExists = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
			String temp;
			long id;
			if( !itemExists.isEmpty() )
			{
				for(Entity thisItem : itemExists)
				{
					Pattern pattern = Pattern.compile("\\(.+?\\)");
		            Matcher matcherValidity = pattern.matcher(thisItem.getKey().toString());
		            if(matcherValidity.find(5)){
		            	temp = matcherValidity.group(0);
		            	temp = temp.replace("(", "");
		            	temp = temp.replace(")","");
		                id = Long.parseLong(temp);
		                if(id == item.getId())
		                	{
		                		Users user = new UserDataSource().getUserById(id);
		                		thisItem.setProperty("Username",user.getName());
		                		thisItem.setProperty("Taskname",item.getName());
		                		thisItem.setProperty("Note", item.getNote());
		                		thisItem.setProperty("Date", item.getDueDate());
		                		thisItem.setProperty("Time", item.getDueTime2());
		                		thisItem.setProperty("Priority",item.getPriority());
		                		thisItem.setProperty("Checked", item.isChecked() );
		                		datastore.put(thisItem);
		                	}
		            }
				}
			}
	  }


	  public List<ToDoItem> getToDoListByUId(String username) {
		  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		  	List<ToDoItem> toDoList = new ArrayList<ToDoItem>();
		  	String tempStr = "";
		    Key dbKey = KeyFactory.createKey("CDB", "itemDB");
		    Filter existFltr = new FilterPredicate("Username" , FilterOperator.EQUAL , username );
			Query query = new Query("items" , dbKey).setFilter(existFltr);
		    List<Entity> items = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
		    long uid = getUserID(username);
		    System.out.println(items.size());
		    if (!items.isEmpty()) {
		        //int count = 1; 
		        for (Entity singleItem : items) {
		        	ToDoItem temp = new ToDoItem();
		        	//temp.setUserId(count++);
		        	Pattern pattern = Pattern.compile("\\(.+?\\)");
		            Matcher matcherValidity = pattern.matcher(singleItem.getKey().toString());
		            if(matcherValidity.find(5)){
		            	tempStr = matcherValidity.group(0);
		            	tempStr = tempStr.replace("(", "");
		            	tempStr = tempStr.replace(")","");
		                temp.setId(Long.parseLong(tempStr));
		            }
		        	temp.setUserId(uid);
		        	temp.setName((String)singleItem.getProperty("Taskname"));
		        	//temp.setName("abcd");
		        	temp.setNote((String)singleItem.getProperty("Note"));
		        	//Remove this
		        	temp.setDueDate((String)singleItem.getProperty("Date"));
		        	temp.setDueTime2((String)singleItem.getProperty("Time"));
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
		    	if(temp.isChecked())
		    	{
		    		list.add(temp);
		    	}
		    }
		    return list;
		  }
	  
	  public List<ToDoItem> getAllToDo() {
		  	System.out.println("yay!");
		  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		  	String tempStr;
		    Key dbKey = KeyFactory.createKey("CDB", "itemDB");
		    List<ToDoItem> list = new ArrayList<ToDoItem>();
		    Query query = new Query("items", dbKey).addSort("Taskname", Query.SortDirection.DESCENDING);
		    List<Entity> items = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
		    System.out.println(items.size());
		    if (!items.isEmpty()) {
		        for (Entity singleItem : items) {
		        	ToDoItem temp = new ToDoItem();
		        	Pattern pattern = Pattern.compile("\\(.+?\\)");
		            Matcher matcherValidity = pattern.matcher(singleItem.getKey().toString());
		            if(matcherValidity.find(5)){
		            	tempStr = matcherValidity.group(0);
		            	tempStr = tempStr.replace("(", "");
		            	tempStr = tempStr.replace(")","");
		                temp.setId(Long.parseLong(tempStr));
		            }
		            long uid = getUserID((String)singleItem.getProperty("Username"));
		        	temp.setUserId(uid);
		        	temp.setName((String)singleItem.getProperty("Taskname"));
		        	//temp.setName("abcd");
		        	temp.setNote((String)singleItem.getProperty("Note"));
		        	//Remove this
		        	temp.setDueDate((String)singleItem.getProperty("Date"));
		        	System.out.println((String)singleItem.getProperty("Date"));
		        	temp.setDueTime2((String)singleItem.getProperty("Time"));
		        	System.out.println((String)singleItem.getProperty("Time"));
		        	//End Remove
		        	temp.setChecked((Boolean) singleItem.getProperty("Checked"));
		        	temp.setPriority((Long)singleItem.getProperty("Priority"));
		        	list.add(temp);
		        }
		    }
		    return list;
		  }
	  //extra functions
	  public static Key getItemID(ToDoItem item){
		  	long id = 0;
		  	Key itemid = null; 
		  	String temp;
		  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		  	Key dbKey = KeyFactory.createKey("CDB", "itemDB");
			Filter existFltr = new FilterPredicate("Taskname" , FilterOperator.EQUAL , item.getName() );
			Query query = new Query("items" , dbKey).setFilter(existFltr);
			List<Entity> itemExists = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
			if( !itemExists.isEmpty() )
			{
				for( Entity thisItem: itemExists ){
				        	Pattern pattern = Pattern.compile("\\(.+?\\)");
				            Matcher matcherValidity = pattern.matcher(thisItem.getKey().toString());
				            if(matcherValidity.find(5)){
				            	temp = matcherValidity.group(0);
				            	temp = temp.replace("(", "");
				            	temp = temp.replace(")","");
				                id = Long.parseLong(temp);
				                if(id == item.getId())
				                	itemid = thisItem.getKey();
				            }
				}
			}
		  return itemid;
	  }
	  public static long getUserID(String username){
		  long userid = 0;
		  String temp = "";
		  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		  	Key dbKey = KeyFactory.createKey("CDB", "userDB");
			Filter existFltr = new FilterPredicate("Username" , FilterOperator.EQUAL , username );
			Query query = new Query("users" , dbKey).setFilter(existFltr);
			List<Entity> usersExist = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
			if( !usersExist.isEmpty() )
			{
				for( Entity thisUser : usersExist ){
				        	Pattern pattern = Pattern.compile("\\(.+?\\)");
				            Matcher matcherValidity = pattern.matcher(thisUser.getKey().toString());
				            if(matcherValidity.find(5)){
				            	temp = matcherValidity.group(0);
				            	temp = temp.replace("(", "");
				            	temp = temp.replace(")","");
				                userid = Long.parseLong(temp);
				            }
				}
			}
		  return userid;
	  }
	  public static ToDoItemDataSource getInstance() {
	      if(instance == null) {
	         instance = new ToDoItemDataSource();
	      }
	      return instance;
	   }
	  
}

