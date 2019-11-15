package com.tkhoma.result;

public class Link {
	private final Integer source;
	private final Integer target;
	private String type;
	
	public Link(Integer source, Integer target, String type) {
		this.source = source;
		this.target = target;
		this.type = type;
	}
	
	public Integer getSource() {
		return source;
	}
	
	public Integer getTarget() {
		return target;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
