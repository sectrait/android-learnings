package com.ex.sqlitepoc.db;

public class Item {
	private int id;
	private String name;
	private ItemGroup itemGroup;

	public Item() {
	}

	public Item(int id, String name, ItemGroup itemGroup) {
		this.id = id;
		this.name = name;
		this.itemGroup = itemGroup;
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
		return "Item [id=" + id + ", name=" + name + ", belongs to itemGroup="
				+ itemGroup + "]";
	}
}
