package com.tkhoma;

import java.util.Arrays;
import java.util.List;

public class Classpath {
	private final List<String> flowFiles;

	public Classpath(String filesPath) {
		String[] files = filesPath.split(";");
		flowFiles = Arrays.asList(files);
	}

	public List<String> getFlowsList() {
		return flowFiles;
	}
}
