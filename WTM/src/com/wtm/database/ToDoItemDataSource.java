package com.wtm.database;

import java.util.ArrayList;
import java.util.List;

public class ToDoItemDataSource {

	  public ToDoItemDataSource() {
	  }
	  
	  public ToDoItem getItemByItemId(long id) { //id 
		  ToDoItem item = new ToDoItem(); 
		  return item;
	  }
	  

	  public void createItem(ToDoItem item) { //change to values
	    
	  }
	  
	  public void deleteItem(ToDoItem item) {
		  
	  }
	  
	  public void updateItem(ToDoItem item) {
		  
	  }


	  public List<ToDoItem> getToDoListByUId(long userId) {
	    List<ToDoItem> list = new ArrayList<ToDoItem>();
	    return list;
	  }


	  public List<ToDoItem> getInCompleteListByUId(long userId) {
		    List<ToDoItem> list = new ArrayList<ToDoItem>();
		    return list;
		  }
	  
	  public List<ToDoItem> getAllToDo() {
		    List<ToDoItem> list = new ArrayList<ToDoItem>();
		    return list;
		  }
	  
}

