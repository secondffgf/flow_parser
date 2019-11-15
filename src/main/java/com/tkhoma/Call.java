package com.tkhoma;

public class Call {
	private final String parent;
	private final Integer callLine;
	private final String flowRefName;
	private final Integer flowDefLine;
	private final String flowFile;
	
	public Call(String parent, String flowRefName, Integer callLine, Integer flowDefLine, String flowFile) {
		this.parent = parent;
		this.flowRefName = flowRefName;
		this.callLine = callLine;
		this.flowDefLine = flowDefLine;
		this.flowFile = flowFile;
	}
	
	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		toString.append(parent)
			.append(":").append(callLine)
			.append(" ").append(flowRefName)
			.append(":").append(flowDefLine)
			.append(" ").append(flowFile)
			.append(System.lineSeparator());
		return toString.toString();
	}

	public String getFlowRefName() {
		return flowRefName;
	}
	
	public Integer getFlowDefLine() {
		return flowDefLine;
	}

	public Integer getCallLine() {
		return callLine;
	}

	public String getParent() {
		return parent;
	}

	public String getFlowFile() {
		return flowFile;
	}
}
