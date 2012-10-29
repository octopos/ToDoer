package com.wtm.todo;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wtm.database.UserDataSource;

@SuppressWarnings("serial")
public class UpdateAccountServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if( req.getParameter("button").equals("Submit") ){
			String name = req.getParameter("username");
			String password1 = req.getParameter("password1");
			UserDataSource uds = new UserDataSource();				
			
			if( uds.userExists(name) )
			{
				
				resp.sendRedirect("login.jsp");
			}
			else
			{
				// Should print out alert box indicating user already exists
				resp.sendRedirect("createAccount.jsp?error=User already exists!");
			}
		}
		else
		{
			resp.sendRedirect("login.jsp");
		}
	}
}
