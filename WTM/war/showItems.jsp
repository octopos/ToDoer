<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.wtm.database.ToDoItem" %>
<%@ page import="com.wtm.database.ToDoItemDataSource" %>
<%@ page import="java.util.Iterator"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
  <head>
    <link type="text/css" rel="stylesheet" href="../Resources/wtmStyles.css" />
  </head>

  <body>
	<%
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key dbKey = KeyFactory.createKey("CDB", "itemDB");
    Query query = new Query("items", dbKey).addSort("Taskname", Query.SortDirection.DESCENDING);
    List<Entity> items = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
    if (items.isEmpty()) {
        %>
        <p>There are no items.</p>
        <%
    } else {
        %>
        <p>Items in database.</p>
        <%
        for (Entity singleItem : items) {
       		long temp = ToDoItemDataSource.getUserID((String)session.getAttribute("User"));
        	pageContext.setAttribute("item_uid",temp);
        	pageContext.setAttribute("item_key",singleItem.getKey());
        	
        	String user = (String)singleItem.getProperty("Username"); //test string cast - Works!
        	pageContext.setAttribute("item_user" ,user);
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
        		
                <p>:${(item_uid)}:${(item_key)}:${(item_user)}:${(item_name)}: ${(item_note)} :${(item_date)}: ${(item_time)}: ${(item_priority)}</p>

        <%
        }
    }
    
   
    
%>

<%	
	List <ToDoItem> list = ToDoItemDataSource.getInstance().getToDoListByUId((String)session.getAttribute("User"));
	    Iterator<ToDoItem> it = list.iterator();
	    ToDoItem temp;
	   while(it.hasNext())
	   {
			temp = it.next();
		   %><%=temp.getId()%>:<%=temp.getUserId()%>:Name-<%=temp.getName()%>:<%=temp.getNote()%>:<%=temp.getPriority()%><br/>
		   <%
	   }
%>
<%	
	List <ToDoItem> list2 = ToDoItemDataSource.getInstance().getInCompleteListByUId((String)session.getAttribute("User"));
	    Iterator<ToDoItem> it2 = list2.iterator();
	    ToDoItem temp2;
	   while(it2.hasNext())
	   {
			temp2 = it2.next();
		   %><%=temp2.getId()%>:<%=temp2.getUserId()%>:Name-<%=temp2.getName()%>:<%=temp2.getNote()%>:<%=temp2.getPriority()%><br/>
		   <%
	   }
%>
  </body>
</html>