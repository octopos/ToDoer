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
    Key dbKey = KeyFactory.createKey("CDB", "itemsDB");
    // Run an ancestor query to ensure we see the most up-to-date
    // view of the Greetings belonging to the selected Guestbook.
    Query query = new Query("items", dbKey).addSort("Taskname", Query.SortDirection.DESCENDING);
    List<Entity> items = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
    if (items.isEmpty()) {
        %>
        <p>There are no items.</p>
        <%
    } else {
        %>
        <p>Items in database.</p>
        <%
        for (Entity singleItem : items) {
        	pageContext.setAttribute("item_user" ,
        			singleItem.getProperty("Username"));
        	pageContext.setAttribute("item_name" ,
        			singleItem.getProperty("Taskname"));
        	pageContext.setAttribute("item_note" ,
        			singleItem.getProperty("Note"));
        	pageContext.setAttribute("item_date" ,
        			singleItem.getProperty("Date"));
        	pageContext.setAttribute("item_time" ,
        			singleItem.getProperty("Time"));
        	pageContext.setAttribute("item_priority" ,
        			singleItem.getProperty("Priority"));
        %>
        		
                <p>${(item_user)}:${(item_name)}: ${(item_note)} :${(item_date)}: ${(item_time)}: ${(item_priority)}</p>

        <%
        }
    }
%>
  </body>
</html>