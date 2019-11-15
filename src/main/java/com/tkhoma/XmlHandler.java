package com.tkhoma;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlHandler {
	private static final String LINE_NUMBER = "line_number";
	
	Map<String, Element> flowsByName = new HashMap<>();
	Map<String, Document> xmlFilesCache = new HashMap<>();

	public Element findFlow(String flowName, String flowFile) {
		Document flowDocument = readFlowFile(flowFile);
		Element element = findTag(flowDocument, flowName, "flow", flowFile);
		if (element == null) {
			element = findTag(flowDocument, flowName, "sub-flow", flowFile);
		}
		return element;
	}
	
	public static String getLineNumber() {
		return LINE_NUMBER;
	}
	
	public List<Entry<String, Integer>> findFlowReferencesInFlow(Element flow) {
		List<Entry<String, Integer>> flowRefs = new ArrayList<>();
		NodeList flowReferences = flow.getElementsByTagName("flow-ref");
		for (int i = 0; i < flowReferences.getLength(); i++) {
			Node flowRefNode = flowReferences.item(i);
			Element flowRef = (Element) flowRefNode;
			String flowName = flowRef.getAttribute("name");
			String lineNumber = flowRef.getAttribute(LINE_NUMBER);
			Entry<String, Integer> entry = new AbstractMap.SimpleEntry<>(flowName, Integer.valueOf(lineNumber));
			flowRefs.add(entry);
		}
		return flowRefs;
	}

	private Document readFlowFile(String fileName) {
		Document documentFromCache = xmlFilesCache.get(fileName);
		if (documentFromCache != null) {
			return documentFromCache;
		}
		InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
		Document doc = null;
		try {
			doc = readXML(is, LINE_NUMBER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		xmlFilesCache.put(fileName, doc);
		return doc;
	}
	
	private Element findTag(Document flowDocument, String flowName, String tag, String fileName) {
		NodeList flows = flowDocument.getElementsByTagName(tag);
		for (int i = 0; i < flows.getLength(); i++) {
			Node element = flows.item(i);
			Element flow = (Element) element;
			if (flow.getAttribute("name").equals(flowName)) {
				String lineNumber = flow.getAttribute(LINE_NUMBER);
				System.out.println("call flow with name: " + flowName + " in file: " + fileName + " on line number: " + lineNumber);
				return flow;
			}
		}
		return null;
	}
	
	private Document readXML(InputStream is, final String lineNumAttribName) throws IOException, SAXException {
	    final Document doc;
	    SAXParser parser;
	    try {
	        SAXParserFactory factory = SAXParserFactory.newInstance();
	        parser = factory.newSAXParser();
	        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        doc = docBuilder.newDocument();           
	    } catch(ParserConfigurationException e){
	        throw new RuntimeException("Can't create SAX parser / DOM builder.", e);
	    }
	 
	    final Stack<Element> elementStack = new Stack<Element>();
	    final StringBuilder textBuffer = new StringBuilder();
	    DefaultHandler handler = new DefaultHandler() {
	        private Locator locator;
	 
	        @Override
	        public void setDocumentLocator(Locator locator) {
	            this.locator = locator; //Save the locator, so that it can be used later for line tracking when traversing nodes.
	        }
	        
	        @Override
	        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {               
	            addTextIfNeeded();
	            Element el = doc.createElement(qName);
	            for(int i = 0;i < attributes.getLength(); i++)
	                el.setAttribute(attributes.getQName(i), attributes.getValue(i));
	            el.setAttribute(lineNumAttribName, String.valueOf(locator.getLineNumber()));
	            elementStack.push(el);               
	        }
	        
	        @Override
	        public void endElement(String uri, String localName, String qName){
	            addTextIfNeeded();
	            Element closedEl = elementStack.pop();
	            if (elementStack.isEmpty()) { // Is this the root element?
	                doc.appendChild(closedEl);
	            } else {
	                Element parentEl = elementStack.peek();
	                parentEl.appendChild(closedEl);                   
	            }
	        }
	        
	        @Override
	        public void characters (char ch[], int start, int length) throws SAXException {
	            textBuffer.append(ch, start, length);
	        }
	        
	        // Outputs text accumulated under the current node
	        private void addTextIfNeeded() {
	            if (textBuffer.length() > 0) {
	                Element el = elementStack.peek();
	                Node textNode = doc.createTextNode(textBuffer.toString());
	                el.appendChild(textNode);
	                textBuffer.delete(0, textBuffer.length());
	            }
	        }           
	    };
	    parser.parse(is, handler);
	    
	    return doc;
	}
	
}
