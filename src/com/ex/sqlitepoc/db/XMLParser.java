package com.ex.sqlitepoc.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.ex.sqlitepoc.R;

public class XMLParser {
	private final String LOG_TAG = getClass().getSimpleName();

	private Resources applicationResources;

	private List<ItemGroup> itemGroups;
	private List<Item> items;

	public XMLParser(Resources res) {
		this.applicationResources = res;
		Log.i(LOG_TAG, "XMLParser Instantiated");
		itemGroups = new ArrayList<ItemGroup>();
		items = new ArrayList<Item>();
	}

	public void parseXMLFromResource() {
		Log.i(LOG_TAG, "Parsing from the resource");

		XmlResourceParser xpp = null;
		boolean groupFlag = true;
		Item item = null;
		ItemGroup itemGroup = null;
		try {
			xpp = applicationResources.getXml(R.xml.groups);
			// check state
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagName = xpp.getName();
				if (eventType == XmlPullParser.START_DOCUMENT) {
					Log.i(LOG_TAG, "Start document");
				} else if (eventType == XmlPullParser.START_TAG
						&& tagName.equals(applicationResources
								.getString(R.string.xml_tag_ItemGroup))) {
					itemGroup = new ItemGroup();
					itemGroup.setId(xpp
							.getAttributeIntValue(null, applicationResources
									.getString(R.string.xml_tag_id), -1));
					groupFlag = true;
				} else if (eventType == XmlPullParser.START_TAG
						&& tagName.equals(applicationResources
								.getString(R.string.xml_tag_Item))) {
					item = new Item();
					item.setId(xpp
							.getAttributeIntValue(null, applicationResources
									.getString(R.string.xml_tag_id), -1));
					groupFlag = false;
				} else if (eventType == XmlPullParser.START_TAG
						&& tagName.equals(applicationResources
								.getString(R.string.xml_tag_name))) {
					eventType = xpp.next();
					if (eventType == XmlPullParser.TEXT && !groupFlag) {
						item.setItemGroup(itemGroup);
						item.setName(xpp.getText());
						items.add(item);
					} else {
						itemGroup.setName(xpp.getText());
						itemGroups.add(itemGroup);
					}
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
		} finally {
			if (xpp != null)
				xpp.close();
		}
		Log.i(LOG_TAG, "Resourced XML Parsed");
		return;
	}

	public List<ItemGroup> getItemGroups() {
		return itemGroups;
	}

	public List<Item> getItems() {
		return items;
	}
}