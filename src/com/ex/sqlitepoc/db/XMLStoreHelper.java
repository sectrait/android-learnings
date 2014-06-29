package com.ex.sqlitepoc.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class XMLStoreHelper extends SQLiteOpenHelper {

	private final String LOG_TAG = getClass().getSimpleName();
	private Context context;
	private boolean databaseCreatedOrUpgraded;

	// Database Name
	private static final String DATABASE_NAME = "items.db";
	private static final int DATABASE_VERSION = 35;

	// Database creation SQL statement For ItemGroup
	public static final String DATABASE_CREATE_ITEM_GROUP = "create table "
			+ ItemGroupDataSource.TABLE_ITEM_GROUP + "("
			+ ItemGroupDataSource.COLUMN_GROUP_ID + " integer primary key,"
			+ ItemGroupDataSource.COLUMN_GROUP_NAME + " text not null);";

	// Database creation SQL statement For Item
	private static final String DATABASE_CREATE_ITEM = "create table "
			+ ItemDataSource.TABLE_ITEM + "(" + ItemDataSource.COLUMN_ITEM_ID
			+ " integer not null," + ItemDataSource.COLUMN_ITEM_NAME
			+ " text not null," + ItemDataSource.COLUMN_ITEM_GROUP
			+ " integer not null " + ", PRIMARY KEY ("
			+ ItemDataSource.COLUMN_ITEM_ID + ") " + ", FOREIGN KEY("
			+ ItemDataSource.COLUMN_ITEM_GROUP + ") REFERENCES "
			+ ItemGroupDataSource.TABLE_ITEM_GROUP + " ("
			+ ItemGroupDataSource.COLUMN_GROUP_ID + "));";

	public XMLStoreHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		databaseCreatedOrUpgraded = false;
	}

	public XMLStoreHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.i(LOG_TAG, "Creating table " + ItemGroupDataSource.TABLE_ITEM_GROUP
				+ " via " + DATABASE_CREATE_ITEM_GROUP);
		database.execSQL(DATABASE_CREATE_ITEM_GROUP);
		Log.i(LOG_TAG, "Creating table " + ItemDataSource.TABLE_ITEM + " via "
				+ DATABASE_CREATE_ITEM);
		database.execSQL(DATABASE_CREATE_ITEM);
		dumpXMLToTables(database);
		databaseCreatedOrUpgraded = true;
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + ItemDataSource.TABLE_ITEM);
		database.execSQL("DROP TABLE IF EXISTS "
				+ ItemGroupDataSource.TABLE_ITEM_GROUP);
		Log.i(LOG_TAG, "Dropping table " + ItemDataSource.TABLE_ITEM);
		Log.i(LOG_TAG, "Dropping table " + ItemGroupDataSource.TABLE_ITEM_GROUP);
		onCreate(database);
	}

	private void dumpXMLToTables(SQLiteDatabase database) {
		ItemDataSource idS = null;
		ItemGroupDataSource igds = null;

		XMLParser xmlParser = new XMLParser(context.getResources());
		xmlParser.parseXMLFromResource();
		idS = new ItemDataSource(database);
		igds = new ItemGroupDataSource(database);
		for (ItemGroup itemGroup : xmlParser.getItemGroups()) {
			igds.insertItemGroup(itemGroup);
		}

		for (Item item : xmlParser.getItems()) {
			idS.insertItem(item);
		}
	}

	public boolean isDatabaseCreatedOrUpgraded() {
		return databaseCreatedOrUpgraded;
	}
}