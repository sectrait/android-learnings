package com.ex.sqlitepoc.ormlite.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author varun A Persistent ItemGroup object that will be in sqlite database
 */
@DatabaseTable(tableName = "ItemGroup")
public class ItemGroup {

	@DatabaseField(columnName = "_id", canBeNull = false, id = true)
	private long id;
	@DatabaseField(columnName = "GroupName", canBeNull = false)
	private String name;

	public ItemGroup(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public ItemGroup(long id) {
		this.id = id;
	}

	public ItemGroup() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ItemGroup [id=" + id + ", name=" + name + "]";
	}
}
