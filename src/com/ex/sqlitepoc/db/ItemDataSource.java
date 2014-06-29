package com.ex.sqlitepoc.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ItemDataSource {
	private final String LOG_TAG = getClass().getSimpleName();

	// Database table Item
	public static final String TABLE_ITEM = "Item";
	public static final String COLUMN_ITEM_ID = "_id";
	public static final String COLUMN_ITEM_NAME = "ItemName";
	public static final String COLUMN_ITEM_GROUP = "GroupId";

	private static final String[] allColumns = { COLUMN_ITEM_ID,
			COLUMN_ITEM_NAME, COLUMN_ITEM_GROUP };

	// Database fields
	private SQLiteDatabase database;

	public ItemDataSource(SQLiteDatabase database) {
		this.database = database;
	}

	public long insertItem(Item item) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_ITEM_ID, item.getId());
		values.put(COLUMN_ITEM_GROUP, item.getItemGroup().getId());
		values.put(COLUMN_ITEM_NAME, item.getName());

		long insertId = database.insert(TABLE_ITEM, null, values);

		return insertId;
	}

	public void deleteItem(Item item) {
		int id = item.getId();
		Log.i(LOG_TAG, "Item deleted with id: " + id);
		database.delete(TABLE_ITEM, COLUMN_ITEM_ID + " = " + id, null);
	}

	public List<Item> getAllItems() {
		List<Item> items = new ArrayList<Item>();

		Cursor cursor = database.query(TABLE_ITEM, allColumns, null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Item item = cursorToItem(cursor);
			items.add(item);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return items;
	}

	private Item cursorToItem(Cursor cursor) {
		Item item = new Item();

		item.setId(cursor.getInt(0));
		item.setName(cursor.getString(1));
		item.setItemGroup(new ItemGroup(cursor.getInt(2)));

		return item;
	}
}