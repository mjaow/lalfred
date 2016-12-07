package org.loda.lalfred;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class TestStringUtils {

	@Test
	public void testToLowerCase() {
		System.out.println(StringUtils.lowerCase("中文ABc*.?哈哈EjAb"));
	}
}
