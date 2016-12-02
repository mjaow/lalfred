package org.loda.lalfred;

import org.junit.Test;

public class TestProxy {

	@Test
	public void testSearchProxy() {
		ISearcher searcher = new TimerHandler<>(new DemoSearcher()).getProxy();

		searcher.sayName("jack");
	}
}
