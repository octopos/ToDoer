<%@page import="com.google.appengine.api.urlfetch.HTTPRequest"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add or Edit Task</title>
<link rel="stylesheet" type="text/css" href="../Resources/wtmStyles.css">
</head>
<body>
	<h1 class="titleStyle">Do-D-Due</h1>
	<form action="login.do" method="get">
		<table align="center">
			<tr>
				<td>Task Name:</td>
				<td><input name="taskname" type="text" /></td>
			</tr>
			<tr>
				<td>Note:</td>
				<td><input name="note" type="text" /></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="hasDueDate"></td>
				<td>Set Due Time? </td>
			</tr>
			<tr>
				<td>Priority:</td>
				<td>
					<input type="radio" name="priority" id="low" /><label for="low" />
						<img src="../Resources/circle_green.png" height="20">
					<input type="radio" name="priority" id="medium" /><label for="medium" />
						<img src="../Resources/circle_yellow.png" height="20">
					<input type="radio" name="priority" id="high" /><label for="high" />
						<img src="../Resources/circle_red.png" height="20">
				</td>
			</tr>

			<tr>
				<!-- This is not correct, should move back to home screen -->
				<td><form action="login.html" method="post"></form>
					<input type="submit" value="Cancel" />
				</td>
				<td><input type="submit" value="Add" /></td>
			</tr>
		</table>
	</form>
	
</body>
</html>