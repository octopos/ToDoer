package com.wtm.todo;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wtm.database.ToDoItem;
import com.wtm.database.ToDoItemDataSource;

@SuppressWarnings("serial")

public class AddEditServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
			doPost(req, resp);
		}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession session = req.getSession(true);
		String method = req.getParameter("method");
		if(method.equals("Delete"))
		{
			ToDoItemDataSource instance = ToDoItemDataSource.getInstance();
			//System.out.println("here");
			instance.deleteItem(instance.getItemByItemId(Long.parseLong(req.getParameter("id"))));
			//System.out.println("again here");
			resp.sendRedirect("list.jsp");
		}
		else if(method.equals("Check"))
		{
			ToDoItemDataSource instance = ToDoItemDataSource.getInstance();
			System.out.println("Checking here");
			instance.checkIt(Long.parseLong(req.getParameter("id")), Boolean.parseBoolean((String)req.getParameter("check")));
			//System.out.println("Checking again here");
		}
		else{
		String user = (String) session.getAttribute("User");
		String name = req.getParameter("taskname");
		String note = req.getParameter("note");
		String date = req.getParameter("datepicker");
		String time = req.getParameter("timepicker");
		int priority = Integer.parseInt(req.getParameter("priority"));
		
		if(method.equals("Edit"))
		{
			ToDoItemDataSource instance = ToDoItemDataSource.getInstance();
			ToDoItem todo = new ToDoItem();
			todo.setId(Long.parseLong(req.getParameter("id")));
			todo.setName(name);
			todo.setUserId(ToDoItemDataSource.getUserID((String)session.getAttribute("User")));
			System.out.println("uid:"+todo.getUserId());
			if (date.endsWith("/")) {
				date = date.substring(0, date.length() - 1);
			}
			System.out.println(date);
			todo.setDueDate(date);
			todo.setDueTime2(time);
			todo.setPriority(priority);
			todo.setNote(note);
			instance.updateItem(todo);
		}
		else if(method.equals("Add"))
		{
			ToDoItemDataSource instance = ToDoItemDataSource.getInstance();
			instance.createItem(user,name, note, date, time, priority);
		}
		
//		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//		Key dbKey = KeyFactory.createKey("CDB", "itemsDB");
//		Filter existFltr = new FilterPredicate("Taskname" , FilterOperator.EQUAL , name );
//		Query query = new Query("items" , dbKey).setFilter(existFltr);
//		List<Entity> itemsExist = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
//			Entity item = new Entity("items" , dbKey);
//			item.setProperty("Username", user);
//			item.setProperty("Taskname", name);
//			item.setProperty("Note", note);
//			item.setProperty("Date", date);
//			item.setProperty("Time", time);
//			item.setProperty("Priority", priority);
//			datastore.put(item);
			resp.sendRedirect("list.jsp");
	}
	}

}
