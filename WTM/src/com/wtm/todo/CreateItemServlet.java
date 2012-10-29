package com.wtm.todo;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wtm.database.ToDoItemDataSource;

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
		ToDoItemDataSource instance = ToDoItemDataSource.getInstance();
		instance.createItem(user,name, note, date, time, priority);
		resp.sendRedirect("list.jsp");
	}
}
