package com.wtm.todo;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.SortDirection;
public class Test {
 public static void main()
 {
	 	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    Key dbKey = KeyFactory.createKey("CDB", "itemsDB");
	    // Run an ancestor query to ensure we see the most up-to-date
	    // view of the Greetings belonging to the selected Guestbook.
	    Query q = new Query("Item").addSort("Taskname", SortDirection.ASCENDING);
		PreparedQuery pq = datastore.prepare(q);
		
		for (Entity result : pq.asIterable()) {
	  	//String key = singleItem.getKey().toString();
	        	
	        	System.out.println(result.getProperty("Username"));
	        	System.out.println(result.getProperty("Taskname"));
	        	System.out.println(result.getProperty("Note"));
	        	System.out.println(result.getProperty("Date"));
	        	System.out.println(result.getProperty("Time"));
	        	System.out.println(result.getProperty("Priority")); 
			}
}
}
