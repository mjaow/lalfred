package org.loda.lalfred;

import java.io.IOException;

import org.junit.Test;

public class TestSys {

	@Test
	public void testExe() throws IOException {
		Runtime.getRuntime().exec("open -a WeChat");
	}
}
