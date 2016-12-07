package org.loda.lalfred.util;

import java.util.List;

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
		if (!a.equals(b)) {
			throw new IllegalArgumentException(a + " and " + b + " are not equal");
		}
	}

	public static void notEqual(Object a, Object b) {
		if (a.equals(b)) {
			throw new IllegalArgumentException(a + " and " + b + " are equal");
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

	public static void equalList(List<Object> list, List<Object> list2) {
		if (list.size() != list2.size()) {
			throw new IllegalArgumentException("size not equal");
		}

		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).equals(list2.get(i))) {
				throw new IllegalArgumentException("not equal at index " + i);
			}
		}
	}
}
