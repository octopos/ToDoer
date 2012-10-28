package com.wtm.todo;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

@SuppressWarnings("serial")

public class AddEditServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
			resp.sendRedirect("error.html");
		}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String name = req.getParameter("taskname");
		String note = req.getParameter("note");
		String date = req.getParameter("datepicker");
		String time = req.getParameter("timepicker");
		int priority = Integer.parseInt(req.getParameter("priority"));
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key dbKey = KeyFactory.createKey("CDB", "itemsDB");
//		Filter existFltr = new FilterPredicate("Taskname" , FilterOperator.EQUAL , name );
//		Query query = new Query("items" , dbKey).setFilter(existFltr);
//		List<Entity> itemsExist = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
			Entity item = new Entity("items" , dbKey);
			item.setProperty("Taskname", name);
			item.setProperty("Note", note);
			item.setProperty("Date", date);
			item.setProperty("Time", time);
			item.setProperty("Priority", priority);
			datastore.put(item);
			resp.sendRedirect("home.jsp");
		}
		
}
