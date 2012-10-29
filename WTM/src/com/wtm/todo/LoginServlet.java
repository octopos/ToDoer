package com.wtm.todo;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wtm.database.UserDataSource;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession session = req.getSession(true);
		String name = req.getParameter("username");
		String pass = req.getParameter("password");
		UserDataSource uds = new UserDataSource();

		if( uds.canLogin(name, pass) ){
			resp.sendRedirect("list.jsp");
			session.setAttribute("User", name);
		} else {
			resp.sendRedirect("login.jsp?error=Incorrect username/password");
		}

	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

	}
}
