package com.tkhoma.result;

import java.util.List;

public class Result {
	private List<FlowReference> nodes;
	private List<Link> links;
	
	public Result(List<FlowReference> nodes, List<Link> links) {
		this.nodes = nodes;
		this.links = links;
	}
	
	public List<FlowReference> getNodes() {
		return nodes;
	}
	
	public List<Link> getLinks() {
		return links;
	}
}
