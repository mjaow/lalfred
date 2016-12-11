package org.loda.lalfred.util;

import java.util.concurrent.CountDownLatch;

public class Utils {

	public static void await() {
		try {
			new CountDownLatch(1).await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
