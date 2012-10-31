package com.wtm.todo;

import com.wtm.database.ToDoItem;
import java.util.Comparator;


public class PriorityComparator implements Comparator<ToDoItem> {

	@Override
	public int compare(ToDoItem one, ToDoItem two) {
		
		if (one.getPriority()==two.getPriority()) {
			
			return (int)(one.getDueTime()-two.getDueTime());
		}
		
		else
			return (int)(one.getPriority()-two.getPriority());

}
}