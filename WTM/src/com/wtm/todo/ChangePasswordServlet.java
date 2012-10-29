package com.wtm.todo;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wtm.database.UserDataSource;

@SuppressWarnings("serial")
public class ChangePasswordServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if( req.getParameter("button").equals("Submit") ){
			HttpSession session = req.getSession(true);
			String name = (String) session.getAttribute("User");
			String oldPassword = req.getParameter("oldPassword");	
			String newPassword = req.getParameter("newPassword");			
			UserDataSource uds = new UserDataSource();
			
			if( oldPassword.equals(newPassword) ){
				resp.sendRedirect("changePassword.jsp?error=The passwords are the same!");
				return;
			}
			if( uds.userExists(name) )
			{
				if( uds.changePassword(name, oldPassword, newPassword) )
				{
					resp.sendRedirect("list.jsp");
				}
				else
				{
					resp.sendRedirect("changePassword.jsp?error=Password incorrect!");
				}
			}
			else
			{
				// Should not ever be in here. Somehow the user has been deleted
				// while inside the task or two users with the same username are
				// created.
			}
		}
		else
		{
			resp.sendRedirect("list.jsp");
		}
	}
}
