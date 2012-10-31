package com.wtm.todo;

import com.wtm.database.ToDoItem;
import java.util.Comparator;


public class PriorityComparator implements Comparator<ToDoItem> {

	@Override
	public int compare(ToDoItem one, ToDoItem two) {
		
		
		if (one.getPriority()==two.getPriority()) {
			DueDateComparator comp = new DueDateComparator();
			return comp.compare(one, two);
		}
		
		else
			return (int) ((-1)*(one.getPriority() - two.getPriority()));

}
}