package com.ex.sqlitepoc.ormlite.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author varun A Persistent Country Object that will be in sqlite database
 */
@DatabaseTable(tableName = "Item")
public class Item {
	@DatabaseField(columnName = "_id", canBeNull = false, id = true)
	private int id;
	@DatabaseField(columnName = "ItemName", canBeNull = false, index = true)
	private String name;

	@DatabaseField(columnName = "GroupId", foreign = true, canBeNull = false)
	private ItemGroup itemGroup;

	public Item() {
	}

	public Item(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemGroup getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(ItemGroup itemGroup) {
		this.itemGroup = itemGroup;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + "] belongs to itemGroup="
				+ itemGroup + "]";
	}

}