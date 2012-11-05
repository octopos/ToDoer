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
<%@ page import="com.wtm.database.UserDataSource" %>
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
        	pageContext.setAttribute("item_key",singleItem.getKey());
        	String user = (String)singleItem.getProperty("Username"); //test string cast - Works!
        	long temp = UserDataSource.getUserID(user);
        	pageContext.setAttribute("item_uid",temp);
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
	List <ToDoItem> list = ToDoItemDataSource.getInstance().getToDoListByUsername((String)session.getAttribute("User"));
	    Iterator<ToDoItem> it = list.iterator();
	    ToDoItem temp;
	   while(it.hasNext())
	   {
	temp = it.next();
%><%=temp.getId()%>:<%=temp.getUserId()%>:Name-<%=temp.getName()%>:<%=temp.getNote()%>:<%=temp.getPriority()%>  <%=temp.isChecked()%><br/>
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
<p>what!!???</p>
<%	
	List <ToDoItem> list3 = ToDoItemDataSource.getInstance().getAllToDo();
	    Iterator<ToDoItem> it3 = list3.iterator();
	    ToDoItem temp3;
	   while(it3.hasNext())
	   {
			temp3 = it3.next();
		   %><p>abcd</p><%=temp3.getId()%>:<%=temp3.getUserId()%>:Name-<%=temp3.getName()%>:<%=temp3.getNote()%>:<%=temp3.getPriority()%><br/>
		   <%
	   }
%>
<%	
	    	ToDoItem temp4 = ToDoItemDataSource.getInstance().getItemByItemId(19);
		   %><p>abcd</p><%=temp4.getId()%>:<%=temp4.getUserId()%>:Name-<%=temp4.getName()%>:<%=temp4.getNote()%>:<%=temp4.getPriority()%><br/>
		   
		   <%
		   //ToDoItemDataSource.getInstance().deleteItem(temp4);
%>

  </body>
</html>