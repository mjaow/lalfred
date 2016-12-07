package org.loda.lalfred;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.loda.lalfred.util.Assert;

public class TestFileEq {

	@Test
	public void testEq() {
		Assert.notEqual(new File("/Users/loda/Documents/c_test/workspace/helloworld/hello.c"),
				new File("/Users/loda/Documents/c_test/workspace/helloworld/.cproject"));
	}

	@Test
	public void testHash() {
		HashMap<File, Integer> count = new HashMap<>();
		put(count, new File("/Users/loda/Documents/book/java/java语言规范javase6.pdf"));

		put(count, new File("/Users/loda/Documents/book/java/Java程序性能优化-葛一鸣.pdf"));

		System.out.println(count);
	}

	private void put(Map<File, Integer> count, File f) {
		Integer ct = count.get(f);
		if (ct == null) {
			ct = 0;
		}
		ct++;
		count.put(f, ct);
	}
}
