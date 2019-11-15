package com.tkhoma;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tkhoma.result.Result;
import com.tkhoma.result.ResultBuilder;

public class FlowHandler {
	public static void main(String ... args) {
		if (args.length != 3) {
			throw new IllegalArgumentException("You should pass classpath, flow name and output file as arguments!");
		}
		FlowParser parser = new FlowParser(args[0], args[1]);
		Map.Entry<String, Element> mainFlow = parser.findMainFlow();
		parser.findFlowReferencesInFlow(mainFlow.getValue());
		List<Call> calls = parser.getCalls();
		
		saveJsonToFile(mainFlow, calls, args[2]);
	}

	private static void saveJsonToFile(Map.Entry<String, Element> mainFlow, List<Call> calls, String outputFileName) {
		ResultBuilder resultBuilder = new ResultBuilder(mainFlow, calls);
		Result result = resultBuilder.build();
		System.out.println(result);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			mapper.writeValue(new File(outputFileName), result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
