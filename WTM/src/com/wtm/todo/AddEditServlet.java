package com.wtm.todo;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
		Entity item = new Entity("items" , dbKey);
		item.setProperty("Taskname", name);
		item.setProperty("Note", note);
		item.setProperty("Date", date);
		item.setProperty("Time", time);
		item.setProperty("Priority", priority);
		datastore.put(item);
		resp.sendRedirect("list.jsp");
	}
		
}
