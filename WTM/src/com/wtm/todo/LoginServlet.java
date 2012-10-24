package com.wtm.todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
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
		doGet(req, resp);
	}
}
