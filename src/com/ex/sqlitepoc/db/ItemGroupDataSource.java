package com.ex.sqlitepoc.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ItemGroupDataSource {
	private final String LOG_TAG = getClass().getSimpleName();

	// Database table ItemGroup
	public static final String TABLE_ITEM_GROUP = "ItemGroup";
	public static final String COLUMN_GROUP_ID = "_id";
	public static final String COLUMN_GROUP_NAME = "GroupName";

	// Database fields
	private SQLiteDatabase database;
	private final static String[] allColumns = { COLUMN_GROUP_ID,
			COLUMN_GROUP_NAME };

	public ItemGroupDataSource(SQLiteDatabase database) {
		this.database = database;
	}

	public long insertItemGroup(ItemGroup itemgroup) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_GROUP_ID, itemgroup.getId());
		values.put(COLUMN_GROUP_NAME, itemgroup.getName());

		long insertId = database.insert(TABLE_ITEM_GROUP, null, values);

		return insertId;
	}

	public void deleteItemGroup(ItemGroup itemgroup) {
		int id = itemgroup.getId();
		Log.i(LOG_TAG, "ItemGroup deleted with id: " + id);
		database.delete(TABLE_ITEM_GROUP, COLUMN_GROUP_ID + " = " + id, null);
	}

	public List<ItemGroup> getAllItems() {
		List<ItemGroup> itemGroups = new ArrayList<ItemGroup>();

		Cursor cursor = database.query(TABLE_ITEM_GROUP, allColumns, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ItemGroup itemGroup = cursorToItemGroup(cursor);
			itemGroups.add(itemGroup);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return itemGroups;
	}

	private ItemGroup cursorToItemGroup(Cursor cursor) {
		/*
		 * Log.i(LOG_TAG, cursor.toString() + " " + cursor.getColumnCount() +
		 * " " + cursor.getCount() + " " + cursor.getColumnIndex(allColumns[0])
		 * + " " + cursor.getColumnIndex(allColumns[1])); for (String column :
		 * cursor.getColumnNames()) Log.i(LOG_TAG, column);
		 */

		ItemGroup itemgroup = new ItemGroup();

		itemgroup.setId(cursor.getInt(cursor.getColumnIndex(allColumns[0])));
		itemgroup
				.setName(cursor.getString(cursor.getColumnIndex(allColumns[1])));

		return itemgroup;
	}
}
