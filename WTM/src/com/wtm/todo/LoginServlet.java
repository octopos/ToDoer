package com.wtm.todo;

import java.io.IOException;
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
public class LoginServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession session = req.getSession(true);
		String name = req.getParameter("username");
		String pass = req.getParameter("password");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key dbKey = KeyFactory.createKey("CDB", "userDB");
		Filter existFltr = new FilterPredicate("Username" , FilterOperator.EQUAL , name );
		Query query = new Query("users" , dbKey).setFilter(existFltr);
		List<Entity> usersExist = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
		if( !usersExist.isEmpty() )
		{
			for ( Entity thisUser : usersExist ){
				String returnedPassword = (String) thisUser.getProperty("Password");
				if( pass.equals(returnedPassword) ){
					session.setAttribute("User", thisUser.getProperty("Username"));
					resp.sendRedirect("home.jsp");
				}
			}
		} else {
			resp.sendRedirect("error.html");
		}
		
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
	}
}
