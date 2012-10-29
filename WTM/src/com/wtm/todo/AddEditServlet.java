package com.wtm.todo;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.wtm.database.ToDoItemDataSource;

@SuppressWarnings("serial")

public class AddEditServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
			resp.sendRedirect("error.html");
		}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession session = req.getSession(true);
		String user = (String) session.getAttribute("User");
		String name = req.getParameter("taskname");
		String note = req.getParameter("note");
		String date = req.getParameter("datepicker");
		String time = req.getParameter("timepicker");
		int priority = Integer.parseInt(req.getParameter("priority"));
		ToDoItemDataSource instance = ToDoItemDataSource.getInstance();
		instance.createItem(user,name, note, date, time, priority);
//		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//		Key dbKey = KeyFactory.createKey("CDB", "itemsDB");
//		Filter existFltr = new FilterPredicate("Taskname" , FilterOperator.EQUAL , name );
//		Query query = new Query("items" , dbKey).setFilter(existFltr);
//		List<Entity> itemsExist = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
//			Entity item = new Entity("items" , dbKey);
//			item.setProperty("Username", user);
//			item.setProperty("Taskname", name);
//			item.setProperty("Note", note);
//			item.setProperty("Date", date);
//			item.setProperty("Time", time);
//			item.setProperty("Priority", priority);
//			datastore.put(item);
			resp.sendRedirect("list.jsp?time="+time+"&date="+date);
	}

}
