package com.tkhoma.result;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Element;

import com.tkhoma.Call;
import com.tkhoma.XmlHandler;

public class ResultBuilder {

	private List<Call> calls;
	private Entry<String, Element> mainFlow;

	public ResultBuilder(Entry<String, Element> mainFlow, List<Call> calls) {
		this.calls = calls;
		this.mainFlow = mainFlow;
	}

	public Result build() {
		List<FlowReference> nodes = new ArrayList<>();
		Map<String, Integer> nodeIds = new HashMap<>();
		int id = 1;
		
		String mainFlowName = mainFlow.getValue().getAttribute("name");
		Integer flowDefLine = Integer.valueOf(mainFlow.getValue().getAttribute(XmlHandler.getLineNumber()));
		FlowReference flowReference = buildFlowReference(mainFlowName, flowDefLine, mainFlow.getKey(), id++);
		nodes.add(flowReference);
		nodeIds.put(flowReference.getName(), flowReference.getId());
		Set<Integer> sources = new HashSet<>();
		
		Map<String, Link> linksPerConnection = new HashMap<>();
		Link link;
		
		for (Call call : calls) {
			String flowName = call.getFlowRefName();
			if (!nodeIds.containsKey(flowName)) {
				flowReference = buildFlowReference(flowName, call.getFlowDefLine(), call.getFlowFile(), id++);
				nodes.add(flowReference);
				nodeIds.put(flowReference.getName(), flowReference.getId());
			}
			
			String parent = call.getParent();
			Integer source = nodeIds.get(parent);
			Integer target = nodeIds.get(flowName);
			StringBuilder connectionBuilder = new StringBuilder();
			connectionBuilder.append(source);
			connectionBuilder.append(" ");
			connectionBuilder.append(target);
			String connection = connectionBuilder.toString();
			
			Link existingLink = linksPerConnection.get(connection.toString());
			String type = "line " + call.getCallLine();
			if (existingLink != null) {
				if (!type.equals(existingLink.getType())) {
					StringBuilder newType = new StringBuilder(existingLink.getType())
							.append(", ").append(call.getCallLine());
					existingLink.setType(newType.toString());
				}
			} else {
				link = new Link(source, target, type);
				linksPerConnection.put(connection.toString(), link);
			}
			sources.add(source);
		}
		
		nodes.stream()
			.filter(n -> !sources.contains(n.getId()))
			.forEach(n -> n.setLeaf(true));

		Result result = new Result(nodes, new ArrayList<>(linksPerConnection.values()));
		return result;
	}
	
	private FlowReference buildFlowReference(String flowName, Integer flowDefLine, String flowFilePath, int id) {
		StringBuilder location = new StringBuilder();
		String fileName = getFileName(flowFilePath);
		location.append(fileName);
		location.append(":");
		location.append(flowDefLine);
		FlowReference flowReference = new FlowReference(flowName, location.toString(), id);
		return flowReference;
	}

	private String getFileName(String flowFilePath) {
		File flowFile = new File(flowFilePath);
		return flowFile.getName();
	}

}
