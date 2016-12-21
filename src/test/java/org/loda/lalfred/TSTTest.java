package org.loda.lalfred;

import org.junit.Test;

public class TSTTest {

	/**
	 * 
	 * java:[1] php:[2, 6] python:[3, 5] c#:[4] c++:[3] [c#, c++, c] [1, 3]
	 * 
	 */

	/**
	 * delete java:[1] php:[] python:[3, 5] c#:[4] c++:[3] [c#, c++, c] [1, 3]
	 */
	@Test
	public void testTST() {
		TST<Integer> tst = new TST<>();
//		tst.put("java", 1);
//		tst.put("java数据结构", 3);
//		tst.put("php", 2);
//		tst.put("python", 3);
//		tst.put("c#", 4);
//		tst.put("php", 6);
//		tst.put("python", 5);
//		tst.put("c++", 3);
//		tst.put("c", 8);
//		tst.put("编程", 33);
		tst.put("aaa", 1111);
		tst.put("aaaaa", 111111);

//		System.out.println("java:" + tst.get("java"));
//		tst.delete("p", 6);
		tst.delete("aaaaa", 111111);

//		System.out.println("php:" + tst.get("php"));
//		System.out.println("python:" + tst.get("python"));
//		System.out.println("c#:" + tst.get("c#"));
//		System.out.println("c++:" + tst.get("c++"));
		System.out.println("aaa:" + tst.get("aaa"));
		System.out.println("aaaaa:" + tst.get("aaaaa"));

//		System.out.println(tst.keysWithPrefix("p"));
//
//		System.out.println(tst.valuesWithPrefix("p"));

		// Assert.isNull(tst.longestPrefixOf("cc"));
		// Assert.equal(tst.longestPrefixOf("c"), "c++");
		// Assert.equal(tst.get("编程"), 33);
	}

}
