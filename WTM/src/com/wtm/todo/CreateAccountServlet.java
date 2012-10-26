package com.wtm.todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
		String password2 = req.getParameter("password2");
		
		if( !password1.contentEquals(password2) ){
			// Should print out alert box or reload previous page
			
			return;
		}
		Key dbKey = KeyFactory.createKey("CDB", "userDB");
		Entity user = new Entity("users" , dbKey);
		user.setProperty("Username", name);
		user.setProperty("Password", password1);
		
		DatastoreService datastore =
                DatastoreServiceFactory.getDatastoreService();
		datastore.put(user);
		resp.sendRedirect("login.html");
	}
}
