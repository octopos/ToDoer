<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.PreparedQuery" %>
<%@ page import="com.google.appengine.api.datastore.Query.Filter"%>
<%@ page import="com.google.appengine.api.datastore.Query.FilterPredicate"%>
<%@ page import="com.google.appengine.api.datastore.Query.FilterOperator"%>
<%@ page import="com.google.appengine.api.datastore.Query.CompositeFilter"%>
<%@ page import="com.google.appengine.api.datastore.Query.CompositeFilterOperator"%>
<%@ page import="com.google.appengine.api.datastore.Query.SortDirection"%>
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
    Query q = new Query("Item").addSort("Taskname", SortDirection.ASCENDING);
    List<Entity> items = datastore.prepare(q)
                                .asList(FetchOptions.Builder.withDefaults());
	if (items.isEmpty()) {
        %>
        <p>There are no items.</p>
        <%
    } else {
        %>
	<%for (Entity result : items) {
  		String key = result.getKey().toString();
  		pageContext.setAttribute("item_key",key);
        	pageContext.setAttribute("item_user" ,
        			result.getProperty("Username"));
        	pageContext.setAttribute("item_name" ,
        			result.getProperty("Taskname"));
        	pageContext.setAttribute("item_note" ,
        			result.getProperty("Note"));
        	pageContext.setAttribute("item_date" ,
        			result.getProperty("Date"));
        	pageContext.setAttribute("item_time" ,
        			result.getProperty("Time"));
        	pageContext.setAttribute("item_priority" ,
        			result.getProperty("Priority"));
        %>
        		
                <p>${(item_key)}:${(item_user)}:${(item_name)}: ${(item_note)} :${(item_date)}: ${(item_time)}: ${(item_priority)}</p>
	 <%
    }
    }
%>
  </body>
</html>