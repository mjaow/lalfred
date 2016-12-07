package org.loda.lalfred;

import org.junit.Test;

public class TSTTest {

	@Test
	public void testTST() {
		TST<Integer> tst = new TST<>();
		tst.put("java", 1);
		tst.put("java数据结构", 3);
		tst.put("php", 2);
		tst.put("python", 3);
		tst.put("c#", 4);
		tst.put("php", 6);
		tst.put("python", 5);
		tst.put("c++", 3);
		tst.put("c", 8);
		tst.put("编程", 33);
		
		System.out.println(tst.get("java"));
		System.out.println(tst.get("php"));
		System.out.println(tst.get("python"));
		System.out.println(tst.get("c#"));
		System.out.println(tst.get("c++"));

		System.out.println(tst.keysWithPrefix("c"));
		
		System.out.println(tst.valuesWithPrefix("java"));

//		Assert.isNull(tst.longestPrefixOf("cc"));
//		Assert.equal(tst.longestPrefixOf("c"), "c++");
//		Assert.equal(tst.get("编程"), 33);
	}
	
}
