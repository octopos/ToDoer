<%@page import="com.google.appengine.api.urlfetch.HTTPRequest"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List"%>
<%@ page
	import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@ page import="com.google.appengine.api.datastore.DatastoreService"%>
<%@ page import="com.google.appengine.api.datastore.Query"%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ page import="com.google.appengine.api.datastore.FetchOptions"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@ page import="com.wtm.database.ToDoItem"%>
<%@ page import="com.wtm.database.ToDoItemDataSource"%>
<%@ page import="com.wtm.database.UserDataSource"%>
<%@ page import="com.wtm.todo.PriorityComparator"%>
<%@ page import="com.wtm.todo.DueDateComparator"%>
<%@ page import="java.util.Collections"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="javax.servlet.RequestDispatcher;"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
<link rel="stylesheet" type="text/css" href="../Resources/wtmStyles.css">
</head>

<script language="javascript" type="text/javascript">
	function confirmDelete() {
		var r = confirm("Are you sure you want to delete this item?");
		return r;
	}

	function checkIt(cb) {
		var xmlHttp = null;
		xmlHttp = new XMLHttpRequest();
		xmlHttp.open("GET", "addEdit?id=" + cb.id + "&method=Check&check="
				+ cb.checked, false);
		xmlHttp.send();
	}

	function hideCompleted() {
		var inputs = document.getElementsByTagName("input");

		for ( var i = 0; i < inputs.length; ++i) {
			if (inputs[i].type == "checkbox" && inputs[i].name == "taskCkbo"
					&& inputs[i].checked) {
				if (document.getElementById("row" + inputs[i].id).style.display == 'none') {
					document.getElementById("row" + inputs[i].id).style.display = '';
				} else {
					document.getElementById("row" + inputs[i].id).style.display = 'none';
				}
			}
		}

	}
</script>

<body>
	<h1 class="titleStyle">Do-D-Due</h1>
	<table align="center" cellpadding="10" cellspacing="10">
		<tr>

			<td valign="top">
				<table class="bottomBorder" cellpadding="5">
					<tr>
						<td colspan="6"><input type="checkbox"
							onClick="return hideCompleted()"> Hide Completed?</td>
					</tr>
					<%
						long id = 0;
						long tid = 0;
						id = UserDataSource.getUserID((String) session
								.getAttribute("User"));
						List<ToDoItem> list = ToDoItemDataSource.getInstance()
								.getToDoListByUsername(
										(String) session.getAttribute("User"));
						String sort = "";
						if (request.getParameter("sort") != null) {
							sort = request.getParameter("sort");
							if (sort.equals("priority")) {
								Collections.sort(list, new PriorityComparator());
							} else if (sort.equals("date")) {
								System.out.println("date");
								Collections.sort(list, new DueDateComparator());
								
// 								response.sendRedirect("stub.do?id="+id);
							}
						}
						Iterator<ToDoItem> it = list.iterator();
						ToDoItem tasks;

						while (it.hasNext()) {
							tasks = it.next();
							//request.setAttribute("tasksString", tasks);
					%>
					<tr id="row<%=tasks.getId()%>">
						<td><input type="checkbox" name="taskCkbo"
							onClick="checkIt(this)" id="<%=tasks.getId()%>"
							<%=tasks.isChecked() ? "checked" : ""%>></td>
						<td><%=tasks.getName()%></td>
						<td><%=tasks.getDueDate()%></td>
						<td>
							<%
								if (tasks.getPriority() == 0) {
							%> <label for="low"> </label> <img
							src="../Resources/circle_green.png" height="20"> <%
 	} else if (tasks.getPriority() == 1) {
 %> <label for="medium"> </label> <img
							src="../Resources/circle_yellow.png" height="20"> <%
 	} else if (tasks.getPriority() == 2) {
 %> <label for="high"> </label> <img src="../Resources/circle_red.png"
							height="20"> <%
 	}
 %>
						</td>

						<td>
							<form action="EditItem.jsp" method="post">
								<input type="hidden" name="id" value=<%=tasks.getId()%>>
								<input type="submit" name="method" value="Edit" />
								<!--<input type="hidden" name="tasks" value="${tasksString}" />-->
							</form>
						</td>

						<td>
							<form action="addEdit" method="post">
								<input type="hidden" name="id" value=<%=tasks.getId()%>>
								<input type="submit" onclick="return confirmDelete()"
									name="method" value="Delete" />
							</form>
						</td>
					</tr>

					<%
						}
					%>
				</table>
			</td>
			<td>
				<table align="right">
					<tr>
						<td align="right">
							<form action="AddEdit.jsp" method="post">
								<input type="submit" value="Add task">
							</form>
						</td>
					</tr>
					<tr>
						<td align="right"><input type="button"
							value="Sort by Priority"
							onclick="window.location='list.jsp?sort=priority'"></td>
					</tr>
					<tr>
						<td align="right"><input type="button" value="Sort by Date"
							onclick="window.location='list.jsp?sort=date'"></td>
					</tr>
					<tr>
						<td align="right"><form action="changePassword.jsp"
								method="post">
								<input type="submit" value="Change Password" />
							</form></td>
					</tr>
					<tr>
						<td align="right"><form action="login.jsp" method="post">
								<input type="submit" value="Logout">
							</form></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>