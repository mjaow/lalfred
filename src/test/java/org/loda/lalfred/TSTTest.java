package org.loda.lalfred;

import org.junit.Test;
import org.loda.lalfred.util.Assert;

public class TSTTest {

	@Test
	public void testTST() {
		TST<Integer> tst = new TST<>();
		tst.put("java", 1);
		tst.put("php", 2);
		tst.put("python", 3);
		tst.put("c#", 4);
		tst.put("php", 6);
		tst.put("python", 5);
		tst.put("c++", 3);
		tst.put("c", 8);
		tst.put("编程", 33);

		Assert.equal(tst.get("java"), 1);
		Assert.equal(tst.get("php"), 6);
		Assert.equal(tst.get("python"), 5);
		Assert.equal(tst.get("c#"), 4);
		Assert.notNull(tst.get("c++"));

		Assert.equal(tst.size(), 7);

		System.out.println(tst.keysWithPrefix("c"));

		Assert.isNull(tst.longestPrefixOf("cc"));
		Assert.equal(tst.longestPrefixOf("c"), "c++");
		Assert.equal(tst.get("编程"), 33);
	}
}
