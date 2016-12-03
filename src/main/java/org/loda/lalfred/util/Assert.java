package org.loda.lalfred.util;

public class Assert {

	public static void notNull(Object o) {
		if (o == null) {
			throw new IllegalArgumentException("must not be null");
		}
	}

	public static void isTrue(boolean b) {
		if (!b) {
			throw new IllegalArgumentException("must be true");
		}
	}

	public static void equal(Object a, Object b) {
		notNull(a);
		notNull(b);
		if (!a.equals(b)) {
			throw new IllegalArgumentException(a + " and " + b + " are not equal");
		}

	}

	public static void isNull(Object o) {
		if (o != null) {
			throw new IllegalArgumentException("must be null");
		}
	}

	public static void isNoNegative(Number num) {
		notNull(num);
		if (num.doubleValue() < 0) {
			throw new IllegalArgumentException("must be an no negative number");
		}
	}
}
