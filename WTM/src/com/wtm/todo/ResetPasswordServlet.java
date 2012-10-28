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
public class ResetPasswordServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if( req.getParameter("button").equals("Submit") ){
			String name = req.getParameter("username");	
			String password = req.getParameter("password");			

			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Key dbKey = KeyFactory.createKey("CDB", "userDB");
			Filter existFltr = new FilterPredicate("Username" , FilterOperator.EQUAL , name );
			Query query = new Query("users" , dbKey).setFilter(existFltr);
			List<Entity> usersExist = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
			if( usersExist.size() == 1 )
			{
				Entity thisUser = usersExist.get(0);
				thisUser.setProperty("Password", password );
				datastore.put(thisUser);
				resp.sendRedirect("login.jsp");
			}
			else
			{
				resp.sendRedirect("forgotPassword.jsp?error=This user does not exist!");
			}
		}
		else
		{
			resp.sendRedirect("login.jsp");
		}
	}
}
