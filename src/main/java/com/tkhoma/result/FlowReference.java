package com.tkhoma.result;

public class FlowReference {
	private final String name;
	private final String location;
	private final Integer id;
	private Boolean leaf;
	
	public FlowReference(String name, String location, Integer id) {
		this.name = name;
		this.location = location;
		this.id = id;
		this.leaf = Boolean.FALSE;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}
	
	public Boolean isLeaf() {
		return leaf;
	}
}
