package org.loda.lalfred;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class TestFileContent {

	@Test
	public void testPdf() throws FileNotFoundException, IOException {
		List<String> lines = IOUtils.readLines(new FileReader("/Users/loda/Downloads/《HTTP权威指南》高清中文版.pdf"));
		System.out.println(lines.size());
		System.out.println(lines.isEmpty() ? "empty" : lines.get(200));
	}
}
