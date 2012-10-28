<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
  <head>
    <link type="text/css" rel="stylesheet" href="../Resources/wtmStyles.css" />
  </head>

  <body>
	<%
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key dbKey = KeyFactory.createKey("CDB", "userDB");
    // Run an ancestor query to ensure we see the most up-to-date
    // view of the Greetings belonging to the selected Guestbook.
    Query query = new Query("users", dbKey).addSort("Username", Query.SortDirection.DESCENDING);
    List<Entity> users = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
    if (users.isEmpty()) {
        %>
        <p>There are no users.</p>
        <%
    } else {
        %>
        <p>Users in database.</p>
        <%
        for (Entity singleUser : users) {
        	pageContext.setAttribute("user_name" ,
        			singleUser.getProperty("Username"));
        	pageContext.setAttribute("password" ,
        			singleUser.getProperty("Password"));
        %>
                <p>${fn:escapeXml(user_name)}'s password is: ${(password)}</p>

        <%
        }
    }
%>
  </body>
</html>