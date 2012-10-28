package com.wtm.todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
public class CreateItemServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession session = req.getSession(true);
		String user = (String)session.getAttribute("User");
		String name = req.getParameter("taskname");
		String note = req.getParameter("note");
		String date = req.getParameter("datepicker");
		String time = req.getParameter("timepicker");
		int priority = Integer.parseInt(req.getParameter("priority"));
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key dbKey = KeyFactory.createKey("CDB", "itemDB");
		
			Entity item = new Entity("items" , dbKey);
			item.setProperty("Taskname", name);
			item.setProperty("Note", note);
			item.setProperty("Datee", date);
			item.setProperty("Time", time);
			item.setProperty("Priority", priority);
			datastore.put(item);
			resp.sendRedirect("home.jsp");
	}
}
