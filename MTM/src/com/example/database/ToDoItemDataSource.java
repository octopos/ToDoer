package com.example.database;

import java.util.ArrayList;
import java.util.List;

import com.example.synchronize.ChangeTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ToDoItemDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_USERID, MySQLiteHelper.COLUMN_TODONAME,
			MySQLiteHelper.COLUMN_TODONOTE, MySQLiteHelper.COLUMN_DUETIME,
			MySQLiteHelper.COLUMN_NODUETIME, MySQLiteHelper.COLUMN_PRIORITY,
			MySQLiteHelper.COLUMN_CHECKED, };
	private ChangeTracker tracker;

	public ToDoItemDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
		tracker = new ChangeTracker(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void synchronizeChanges()
	{
		open();
		tracker.synchronizeChanges();
		close();
	}
	
	public ToDoItem getItemByItemId(long id) {
		open();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null, null,
				null, null);
		cursor.moveToFirst();
		if (cursor.isAfterLast())
			return null;
		ToDoItem newItem = cursorToToDoItem(cursor);
		close();
		return newItem;
	}

	public ToDoItem createItem(long userid, String name, String note,
			long duetime, boolean noduetime, boolean checked, long priority) {
		open();
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_USERID, userid);
		values.put(MySQLiteHelper.COLUMN_TODONAME, name);
		values.put(MySQLiteHelper.COLUMN_TODONOTE, note);
		values.put(MySQLiteHelper.COLUMN_DUETIME, duetime);
		if (noduetime)
			values.put(MySQLiteHelper.COLUMN_NODUETIME, 1);
		else
			values.put(MySQLiteHelper.COLUMN_NODUETIME, 0);
		values.put(MySQLiteHelper.COLUMN_PRIORITY, priority);
		if (checked)
			values.put(MySQLiteHelper.COLUMN_CHECKED, 1);
		else
			values.put(MySQLiteHelper.COLUMN_CHECKED, 0);

		long insertId = database.insert(MySQLiteHelper.TABLE_TODOLIST, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		ToDoItem newItem = cursorToToDoItem(cursor);
		cursor.close();
		close();
		tracker.createItemChange(newItem);
		return newItem;
	}

	public void deleteItem(ToDoItem item) {
		open();
		tracker.deleteItemChange(item);
		long id = item.getId();
		database.delete(MySQLiteHelper.TABLE_TODOLIST, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
		close();
	}

	public int updateItem(ToDoItem item) {
		open();
		tracker.updateItemChange(item);
		long id = item.getId();

		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TODONAME, item.getName());
		values.put(MySQLiteHelper.COLUMN_TODONOTE, item.getNote());
		values.put(MySQLiteHelper.COLUMN_DUETIME, item.getDueTime());
		if (item.isNoDueTime())
			values.put(MySQLiteHelper.COLUMN_NODUETIME, 1);
		else
			values.put(MySQLiteHelper.COLUMN_NODUETIME, 0);
		values.put(MySQLiteHelper.COLUMN_PRIORITY, item.getPriority());
		if (item.isChecked())
			values.put(MySQLiteHelper.COLUMN_CHECKED, 1);
		else
			values.put(MySQLiteHelper.COLUMN_CHECKED, 0);

		int result =  database.update(MySQLiteHelper.TABLE_TODOLIST, values,
				MySQLiteHelper.COLUMN_ID + " = " + id, null);
		close();
		return result;
	}

	public List<ToDoItem> getToDoListByUId(long userId) {
		open();
		List<ToDoItem> list = new ArrayList<ToDoItem>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
				allColumns, MySQLiteHelper.COLUMN_USERID + " = " + userId,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ToDoItem item = cursorToToDoItem(cursor);
			list.add(item);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		close();
		return list;
	}

	public boolean getCompletedStatus(long taskId) {
		open();
		boolean result = false;
		String query = "SELECT "
				+ MySQLiteHelper.COLUMN_CHECKED + " FROM "
				+ MySQLiteHelper.TABLE_TODOLIST + " WHERE "
				+ MySQLiteHelper.COLUMN_ID + " = " + taskId ;
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		if (cursor.getInt(0) == 1)
			result = true;
		cursor.close();
		close();
		return result;
	}
	
	public List<ToDoItem> getInCompleteListByUId(long userId) {
		open();
		List<ToDoItem> list = new ArrayList<ToDoItem>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
				allColumns, MySQLiteHelper.COLUMN_USERID + " = " + userId
						+ " AND " + MySQLiteHelper.COLUMN_CHECKED + "=0", null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ToDoItem item = cursorToToDoItem(cursor);
			list.add(item);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		close();
		return list;
	}

	public List<ToDoItem> getAllToDo() {
		open();
		List<ToDoItem> list = new ArrayList<ToDoItem>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_TODOLIST,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ToDoItem item = cursorToToDoItem(cursor);
			list.add(item);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		close();
		return list;
	}

	private ToDoItem cursorToToDoItem(Cursor cursor) {
		ToDoItem item = new ToDoItem();
		item.setId(cursor.getLong(0));
		item.setUserId(cursor.getLong(1));
		item.setName(cursor.getString(2));
		item.setNote(cursor.getString(3));
		item.setDueTime(cursor.getLong(4));
		item.setNoDueTime(cursor.getLong(5) == 0 ? false : true);
		item.setPriority(cursor.getLong(6));
		item.setChecked(cursor.getLong(7) == 0 ? false : true);
		return item;
	}
}
