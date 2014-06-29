package com.ex.sqlitepoc.ormlite.data;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.ex.sqlitepoc.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class XMLORMLiteStoreHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "ormitem.db";
	private static final int DATABASE_VERSION = 1;

	private final String LOG_TAG = getClass().getSimpleName();

	private Context context;
	private boolean databaseCreatedOrUpgraded;

	private Dao<ItemGroup, Integer> itemGroupDao = null;
	private Dao<Item, Integer> itemDao = null;

	public XMLORMLiteStoreHelper(Context context, CursorFactory factory) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION,
				R.raw.ormlite_config);
		this.context = context;
	}

	public XMLORMLiteStoreHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.ormlite_config);
		this.context = context;
		this.databaseCreatedOrUpgraded = false;
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, ItemGroup.class);
			TableUtils.createTable(connectionSource, Item.class);
		} catch (SQLException e) {
			Log.e(LOG_TAG, "Unable to create datbases", e);
		}
		dumpXMLToTables(database);
		this.databaseCreatedOrUpgraded = true;
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.w(LOG_TAG, "Database upgrading from version " + oldVersion
					+ " version " + newVersion
					+ ". This may result in drop of all of the old data");
			TableUtils.dropTable(connectionSource, ItemGroup.class, true);
			TableUtils.dropTable(connectionSource, Item.class, true);
			onCreate(database, connectionSource);
		} catch (SQLException e) {
			Log.e(LOG_TAG, "Unable to upgrade database from version "
					+ oldVersion + " to new " + newVersion, e);
		}
	}

	private void dumpXMLToTables(SQLiteDatabase database) {
		XMLParser xmlParser = new XMLParser(context.getResources());
		xmlParser.parseXMLFromResource();
		for (ItemGroup itemGroup : xmlParser.getItemGroups()) {
			try {
				getItemGroupDao().create(itemGroup);
			} catch (SQLException e) {
				Log.e(LOG_TAG, e.getMessage(), e);
			}
		}

		for (Item item : xmlParser.getItems()) {
			try {
				getItemDao().create(item);
			} catch (SQLException e) {
				Log.e(LOG_TAG, e.getMessage(), e);
			}
		}
	}

	public boolean isDatabaseCreatedOrUpgraded() {
		return databaseCreatedOrUpgraded;
	}

	public Dao<ItemGroup, Integer> getItemGroupDao() throws SQLException {
		if (itemGroupDao == null) {
			itemGroupDao = getDao(ItemGroup.class);
		}
		return itemGroupDao;
	}

	public Dao<Item, Integer> getItemDao() throws SQLException {
		if (itemDao == null) {
			itemDao = getDao(Item.class);
		}
		return itemDao;
	}
}
