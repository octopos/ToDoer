<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
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
<link rel="stylesheet" type="text/css" href="../Resources/wtmStyles.css">
</head>
<title>Change Password</title>

<%
	String error=request.getParameter("error");
	if(error==null || error=="null"){
 		error="";
	}
%>

<script>
    function trim(s) 
    {
        return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
    }

    function validate()
    {
        if(trim(document.frmChangePassword.newPassword.value)=="")
        {
          alert("New password cannot be empty!");
          document.frmChangePassword.newPassword.focus();
          return false;
        }
    }
    
</script>

  <body>
	<form name="frmChangePassword" action="/changePassword" method="post">
	<h1 class="titleStyle">Do-D-Due</h1>
		<table align="center">
			<tr>
				<td colspan="2"><%=error%></td>
			</tr>
			<tr>
				<td>Old Password:</td>
				<td><input name="oldPassword" type="password" /></td>
			</tr>
			<tr>
				<td>New Password:</td>
				<td><input name="newPassword" type="password" /></td>
			</tr>
			<tr>
				<td><input type="submit" onclick = "return validate()" name="button" value="Submit"/></td>
				<td><input type="submit" name="button" value="Cancel"/></td>
			</tr>
		</table>
		</form>
  </body>
</html>