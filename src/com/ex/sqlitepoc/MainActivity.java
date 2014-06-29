package com.ex.sqlitepoc;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ex.sqlitepoc.db.Item;
import com.ex.sqlitepoc.db.ItemDataSource;
import com.ex.sqlitepoc.db.ItemGroup;
import com.ex.sqlitepoc.db.ItemGroupDataSource;
import com.ex.sqlitepoc.db.XMLStoreHelper;
import com.ex.sqlitepoc.ormlite.data.XMLORMLiteStoreHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;

public class MainActivity extends Activity {

	private final String LOG_TAG = getClass().getSimpleName();
	private FetchFromDatabase task;
	private XMLORMLiteStoreHelper xmlormLiteStoreHelper = null;

	private TextView textView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG, "in onCreate");
		setContentView(R.layout.main);
		textView = (TextView) findViewById(R.id.TextView01);
		task = (FetchFromDatabase) new FetchFromDatabase().execute();
		
		xmlormLiteStoreHelper = OpenHelperManager.getHelper(getApplicationContext(), XMLORMLiteStoreHelper.class);
		
		try {
			CloseableIterator<com.ex.sqlitepoc.ormlite.data.Item> iterator;
			Dao<com.ex.sqlitepoc.ormlite.data.Item, Integer> itemDao = xmlormLiteStoreHelper.getItemDao();
			iterator = itemDao.iterator();
			Log.i(LOG_TAG,"database upgraded or not " + xmlormLiteStoreHelper.isDatabaseCreatedOrUpgraded() );
			while(iterator.hasNext()) {
				Log.v(LOG_TAG,"Came From DAO " + iterator.next().toString());
			}
		} catch (SQLException e) {
			Log.i(LOG_TAG, e.getMessage(),e);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		task.cancel(true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(LOG_TAG, "in onDestroy");
	}

	private class FetchFromDatabase extends AsyncTask<Void, String, String> {
		private String textViewText;

		@Override
		protected void onPreExecute() {
			textViewText = (String) textView.getText();
			textViewText += "Fetch from Database started at "
					+ DateFormat.getDateTimeInstance().format(new Date())
					+ "\n";
			textView.setText(textViewText);
		}

		@Override
		protected String doInBackground(Void... arg0) {
			XMLStoreHelper xmlStoreHelper = null;
			ItemDataSource idS = null;
			ItemGroupDataSource igds = null;
			String result = null;
			try {
				xmlStoreHelper = new XMLStoreHelper(getApplicationContext());
				SQLiteDatabase database = xmlStoreHelper.getWritableDatabase();
				textViewText += "Database object created at "
						+ DateFormat.getDateTimeInstance().format(new Date())
						+ "\nwith database uploaded "
						+ xmlStoreHelper.isDatabaseCreatedOrUpgraded();
				publishProgress(textViewText);
				idS = new ItemDataSource(database);
				igds = new ItemGroupDataSource(database);
				List<ItemGroup> itemGroups = igds.getAllItems();
				List<Item> item = idS.getAllItems();
				result = "Total Number of items Fetched From Database :: "
						+ item.size()
						+ "\nTotal Number of groups Fetched From Database :: "
						+ itemGroups.size();
			} finally {
				if (xmlStoreHelper != null)
					xmlStoreHelper.close();
			}
			return result;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			textView.setText(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			textView.setText(textViewText + "\n" + result);
		}
	}
}