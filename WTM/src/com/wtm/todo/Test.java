package com.wtm.todo;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.wtm.database.ToDoItem;
import com.wtm.database.ToDoItemDataSource;
public class Test {
 public static void main()
 {
	 	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	    // Run an ancestor query to ensure we see the most up-to-date
	    // view of the Greetings belonging to the selected Guestbook.
	    Query q = new Query("Item").addSort("Taskname", SortDirection.ASCENDING);
		PreparedQuery pq = datastore.prepare(q);
		
		for (Entity result : pq.asIterable()) {
	  	//String key = singleItem.getKey().toString();
	        	
	        	System.out.println(result.getProperty("Username"));
	        	result.getProperty("Username").toString();
	        	System.out.println(result.getProperty("Taskname"));
	        	System.out.println(result.getProperty("Note"));
	        	System.out.println(result.getProperty("Date"));
	        	System.out.println(result.getProperty("Time"));
	        	System.out.println(result.getProperty("Priority")); 
			}
		List <ToDoItem> list = ToDoItemDataSource.getInstance().getToDoListByUsername("Anju");
	    Iterator<ToDoItem> it = list.iterator();
	   while(it.hasNext())
	   {
		   it.next().toString();
		   list.get(1);
	   }
	   ToDoItem temp = new ToDoItem();
	   temp.getId();
	   temp.getUserId();
	   temp.getName();
	   temp.getNote();
	   temp.getPriority();
	   
	   
	   
}
}
