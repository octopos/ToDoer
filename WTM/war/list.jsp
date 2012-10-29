<%@page import="com.google.appengine.api.urlfetch.HTTPRequest"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
<link rel="stylesheet" type="text/css" href="../Resources/wtmStyles.css">
</head>
<body>
	<h1 class="titleStyle">Do-D-Due</h1>
	<table align="center" cellpadding="10" cellspacing="10">
		<tr>
			<td valign="top">
				<table class="bottomBorder" cellpadding="5">
					<tr>
						<td><input type="checkbox"></td>
						<td>Task 1</td>
						<td><img src="../Resources/circle_green.png" height="20"></td>
						<td><input type="button" value="Edit"></input></td>
						<td><input type="button" value="Delete"></input></td>
					</tr>
					<tr>
						<td><input type="checkbox"></td>
						<td>Task 2</td>
						<td><img src="../Resources/circle_red.png" height="20"></td>
						<td><input type="button" value="Edit"></input></td>
						<td><input type="button" value="Delete"></input></td>
					</tr>
					<tr>
						<td><input type="checkbox"></td>
						<td>Task 3</td>
						<td><img src="../Resources/circle_yellow.png" height="20"></td>
						<td><input type="button" value="Edit"></input></td>
						<td><input type="button" value="Delete"></input></td>
					</tr>
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
						<td align="right"><input type="button" value="Sort by date"></td>
					</tr>
					<tr>
						<td align="right"><input type="button"
							value="Sort by Priority"></td>
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