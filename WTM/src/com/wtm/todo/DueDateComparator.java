package com.wtm.todo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.wtm.database.ToDoItem;

public class DueDateComparator implements Comparator<ToDoItem> {

	Date dateOne;
	Date dateTwo;

	@Override
	public int compare(ToDoItem one, ToDoItem two) {

		String oneStr = one.getDueDate();
		String twoStr = two.getDueDate();
		if(oneStr.equals(twoStr))
			return 0;
		if(oneStr.isEmpty() || oneStr.equals(""))
			return 1;
		if(twoStr.isEmpty() || oneStr.equals(""))
			return -1;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		try {
			dateOne = dateFormat.parse(one.getDueDate());
			dateTwo = dateFormat.parse(two.getDueDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if ((dateOne.compareTo(dateTwo)) == 0) {
			return (int) (one.getPriority() - two.getPriority());
		} else
			return (int) (dateOne.compareTo(dateTwo));
	}
}