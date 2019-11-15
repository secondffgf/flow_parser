package com.tkhoma;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

public class FlowParser {
	private final String mainFlow;
	private final Classpath classpath;
	private final XmlHandler xmlHandler;
	private final List<Call> calls;

	public FlowParser(String filesPath, String flow) {
		mainFlow = flow;
		classpath = new Classpath(filesPath);
		xmlHandler = new XmlHandler();
		calls = new ArrayList<>();
	}
	
	public Map.Entry<String, Element> findMainFlow() {
		return findFlow(mainFlow);
	}
	
	public List<Call> getCalls() {
		return calls;
	}
	
	public void findFlowReferencesInFlow(Element flow) {
		List<Map.Entry<String, Integer>> flowReferences = xmlHandler.findFlowReferencesInFlow(flow);
		// TODO: add final field
		Call call;
		for (Map.Entry<String, Integer> entry : flowReferences) {
			String parent = flow.getAttribute("name");
			String flowRef = entry.getKey();
			Integer callLine = entry.getValue();
			System.out.println(entry.getKey() + ":" + entry.getValue());
			Map.Entry<String, Element> childFlow = findFlow(entry.getKey());
			String defLineNumber = childFlow.getValue().getAttribute(XmlHandler.getLineNumber());
			call = new Call(parent, flowRef, callLine, Integer.valueOf(defLineNumber), childFlow.getKey());
			calls.add(call); 
			findFlowReferencesInFlow(childFlow.getValue());
		}
	}
	
	private Map.Entry<String, Element> findFlow(String flowName) {
		for (String nextFlowFile : classpath.getFlowsList()) {
			Element element = xmlHandler.findFlow(flowName, nextFlowFile);
			if (element != null) {
				return new AbstractMap.SimpleEntry<>(nextFlowFile, element);
			}
		}
		throw new IllegalArgumentException("Flow " + flowName + " is not found!");
	}
}
