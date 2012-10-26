<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
  <head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  </head>

  <body>
	<form action="password.do" method="post">
		<h1 class="titleStyle">Do-D-Due</h1>
		<table align="center">
			<tr>
				<td>Old Password:</td>
				<td><input name="password" type="password" /></td>
			</tr>
			<tr>
				<td>New Password:</td>
				<td><input name="password" type="password" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="Submit"/></td>
				<td><input type="submit" value="Cancel"/></td>
			</tr>
		</table>
		</form>
  </body>
</html>