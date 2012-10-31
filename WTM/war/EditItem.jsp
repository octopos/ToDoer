<%@page import="com.google.appengine.api.urlfetch.HTTPRequest"%>
<%@ page import="com.wtm.database.ToDoItem"%>
<%@ page import="com.wtm.database.ToDoItemDataSource"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add or Edit Task</title>
<link rel="stylesheet" type="text/css" href="../Resources/wtmStyles.css">
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.8.2.js"></script>
<script src="http://code.jquery.com/ui/1.9.1/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/demos/style.css" />
<!--
    Copyright (c) 2007-2012 Paul T. (purtuga.com)
    
    $Date: 2012/08/05 19:40:25 $
    $Author: paulinho4u $
    $Revision: 1.1 $
	-->
<link rel="stylesheet" type="text/css"
	href="../Resources/style/jquery.ptTimeSelect.css" />
<script type="text/javascript"
	src="../Resources/scripts/jquery.ptTimeSelect.js"></script>
<script>
	$(function() {
		$("#datepicker").datepicker();
	});
	$(document).ready(function() {
		// find the input fields and apply the time select to them.
		$('#timepicker').ptTimeSelect();
	});
</script>
<script>
	function validate() {
		if (trim(document.frmCreateItem.taskname.value) == "") {
			alert("Taskname cannot be empty!");
			document.frmCreateAcct.username.focus();
			error = "You must have a taskname";
			return false;
		}
	}
</script>


</head>
<body>
	<h1 class="titleStyle">Do-D-Due</h1>
	<%
		long id = 0;
		id = Long.parseLong(request.getParameter("id"));
		ToDoItem item = ToDoItemDataSource.getInstance()
				.getItemByItemId(id);
		String dueDate = item.getDueDate();
		if (dueDate.endsWith("/")) {
			dueDate = dueDate.substring(0, dueDate.length() - 1);
		}
	%>
	<form name="frmCreateItem" onSubmit="validate()" action="addEdit"
		method="post">
		<table align="center">
			<tr>
				<td>Task Name:</td>
				<td><input name="taskname" type="text"
					value=<%=item.getName()%> /></td>
			</tr>
			<tr>
				<td>Note:</td>
				<td><input name="note" type="text" value=<%=item.getNote()%> /></td>
			</tr>
			<tr>
				<td>Date:</td>
				<td><input name="datepicker" type="text" id="datepicker"
					value=<%=dueDate%> /></td>
			</tr>
			<tr>
				<td>Due Time:</td>
				<td><input name="timepicker" id="timepicker"
					value=<%=item.getDueTime2()%> /> <br /></td>
			</tr>
			<tr>
				<td>Priority:</td>
				<td><input type="radio" name="priority" id="low" value="1"
					<%if (item.getPriority() == 1) {%> checked <%}%> /><label for="low" /></label>
					<img src="../Resources/circle_green.png" height="20"> <input
					type="radio" name="priority" id="medium" value="2"
					<%if (item.getPriority() == 2) {%> checked <%}%> /><label
					for="medium" /></label> <img src="../Resources/circle_yellow.png"
					height="20"> <input type="radio" name="priority" id="high"
					value="3" <%if (item.getPriority() == 3) {%> checked <%}%> /><label
					for="high" /></label> <img src="../Resources/circle_red.png" height="20">
				</td>
			</tr>

			<tr>
				<!-- This is not correct, should move back to home screen -->
				<td><form action="list.jsp" method="post">
						<input type="submit" value="Cancel" />
					</form></td>
				<td><input type="hidden" name="id" value=<%=id%>><input
					type="hidden" name="method" value="Edit" /><input type="submit"
					value="Edit" /></td>
			</tr>
		</table>
	</form>
	<%%>
</body>
</html>