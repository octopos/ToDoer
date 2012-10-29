package com.example.synchronize;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.database.MySQLiteHelper;

public class TaskChangesDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_TCTASKID,
			MySQLiteHelper.COLUMN_TCACTION, MySQLiteHelper.COLUMN_TCTIME };

	public TaskChangesDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	public void createTaskChange(TaskChange change) {
		open();
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TCTASKID, change.getTaskID());
		values.put(MySQLiteHelper.COLUMN_TCACTION, change.getAction()
				.toString());
		values.put(MySQLiteHelper.COLUMN_TCTIME, change.getTimestamp());
		database.insert(MySQLiteHelper.TABLE_TASKCHANGES, null, values);
		System.out.println("Created change item for "+change.getTaskID());
		close();
	}

	public boolean doesTaskHaveAction(long taskid,ActionType action) {
		open();
		boolean result = false;
		String query = "SELECT COUNT(*) FROM "
				+ MySQLiteHelper.TABLE_TASKCHANGES + " WHERE "
				+ MySQLiteHelper.COLUMN_TCTASKID + " = " + taskid + " AND "
				+ MySQLiteHelper.COLUMN_TCACTION + " = \'"+action+"\'";
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		if (cursor.getInt(0) == 1)
			result = true;
		cursor.close();
		close();
		System.out.printf("Does %s task have %s change? %s \n", taskid,action.toString(),result);
		return result;
	}

	public void updateTaskChange(long taskid, String time) {
		open();
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TCTIME, time);
		database.update(MySQLiteHelper.TABLE_TASKCHANGES, values,
				MySQLiteHelper.COLUMN_TCTASKID + " = " + taskid, null);
		System.out.println("Updated change item for "+taskid);
		close();
	}

	public ArrayList<TaskChange> getAllTaskChanges() {
		open();
		ArrayList<TaskChange> allTaskChange = new ArrayList<TaskChange>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_TASKCHANGES,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TaskChange change = cursorToTaskChange(cursor);
			allTaskChange.add(change);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		close();
		return allTaskChange;
	}

	public void deleteChangesByTaskID(long taskid) {
		open();
		System.out.println("change deleted with id: " + taskid);
		String condition = MySQLiteHelper.COLUMN_TCTASKID + " = " + taskid;
		database.delete(MySQLiteHelper.TABLE_TASKCHANGES, condition, null);
		System.out.println("Deleted changes for "+taskid);
		close();
	}
	
	public void deleteAllChanges() {
		open();
		System.out.println("Deleted all changes");
		database.delete(MySQLiteHelper.TABLE_TASKCHANGES, null, null);
		close();
	}

	private TaskChange cursorToTaskChange(Cursor cursor) {
		TaskChange change = new TaskChange(cursor.getLong(0),
				cursor.getString(1), cursor.getString(2));
		return change;
	}

}
