package com.changeme.todolist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.changeme.todolist.model.ToDoTask;
import com.changeme.todolist.sql.MyOpenSQLHelper;
import com.changeme.todolist.sql.ToDoContentProvider;

/**
 * 数据库操作类
 * 
 * @author ldc
 * 
 */
public class ToDoTaskDao {
	private static ToDoTaskDao instance;
	private MyOpenSQLHelper myOpenSQLHelper;

	private ToDoTaskDao(Context context) {
		myOpenSQLHelper = new MyOpenSQLHelper(context);
	}

	public static ToDoTaskDao getInstance(Context context) {
		if (instance == null) {
			instance = new ToDoTaskDao(context);
		}
		return instance;
	}

	/**
	 * 将TodaoTask记录保存到数据库中。
	 * 
	 * @param item
	 */
	public void insert(ToDoTask item) {
		SQLiteDatabase db = myOpenSQLHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(ToDoContentProvider.NAME_COLUMN, item.getName());
		contentValues
				.put(ToDoContentProvider.DATE_COLUMN, item.getCreateDate());
		contentValues.put(ToDoContentProvider.PLANDAY_COLUMN,
				item.getPlanDays());
		contentValues.put(ToDoContentProvider.HABBIT_COLUMN, item.isHabbit());
		contentValues.put(ToDoContentProvider.TODAY_ISDO_COLUMN,
				item.isTodayIsDo());
		contentValues.put(ToDoContentProvider.DURING_DATE_COLUMN,
				item.getDuringDays());
		contentValues.put(ToDoContentProvider.INTERRUPT_DAY_COLUMN,
				item.getInterruptedDays());
		contentValues.put(ToDoContentProvider.IS_COMPLETED_COLUMN,
				item.isCompleted());
		db.insert(MyOpenSQLHelper.TABLE_NAME, null, contentValues);
		db.close();
	}
	
}
