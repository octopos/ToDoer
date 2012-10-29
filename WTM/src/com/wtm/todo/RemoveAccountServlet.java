package com.wtm.todo;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wtm.database.UserDataSource;

@SuppressWarnings("serial")
public class RemoveAccountServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if( req.getParameter("button").equals("Submit") ){
			String name = req.getParameter("username");	
			UserDataSource uds = new UserDataSource();		

			if( uds.userExists(name) )
			{
				uds.deleteUser(name);
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
