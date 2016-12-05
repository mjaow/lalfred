package org.loda.lalfred;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class TestIndexManager {

	@Test
	public void testIndexManager() {
		IndexManager manager = new IndexManager();
		File f = new File("/Users/loda/Documents/book/数学和算法/编程之美.pdf");
		manager.buildIndex(f);
		List<File> list = manager.getByPrefix("美");

		for (File file : list) {
			System.out.println(file.getName());
		}
	}
}
