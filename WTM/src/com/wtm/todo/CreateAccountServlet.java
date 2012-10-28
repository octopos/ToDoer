package com.wtm.todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
public class CreateAccountServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String name = req.getParameter("username");
		//String pass = req.getParameter("password");
		ArrayList<String> names = new ArrayList<String>(Arrays.asList("abc",
				"me", "you"));
		if (names.contains(name)) {
			/*
			RequestDispatcher view = req.getRequestDispatcher("home.jsp");
			try {
				view.forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			resp.sendRedirect("home.jsp");
		} else {
			resp.sendRedirect("error.html");
		}
		
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String name = req.getParameter("username");
		String password1 = req.getParameter("password1");				
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key dbKey = KeyFactory.createKey("CDB", "userDB");
		Filter existFltr = new FilterPredicate("Username" , FilterOperator.EQUAL , name );
		Query query = new Query("users" , dbKey).setFilter(existFltr);
		List<Entity> usersExist = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
		if( usersExist.isEmpty() )
		{
			Entity user = new Entity("users" , dbKey);
			user.setProperty("Username", name);
			user.setProperty("Password", password1);
			datastore.put(user);
			resp.sendRedirect("login.html");
		}
		else
		{
			// Should print out alert box indicating user already exists
			req.setAttribute("error" , "User already exists!");
			resp.sendRedirect("createAccount.jsp?error=User already exists!");
		}
	}
}
