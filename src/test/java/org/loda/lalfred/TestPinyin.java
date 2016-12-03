package org.loda.lalfred;

import java.util.Arrays;

import org.junit.Test;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class TestPinyin {

	@Test
	public void testPy() throws BadHanyuPinyinOutputFormatCombination {
		String name = "大学name";
		for (char ch : name.toCharArray()) {
			String[] s = PinyinHelper.toHanyuPinyinStringArray(ch);
			System.out.println(Arrays.toString(s));
		}

		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		String s = PinyinHelper.toHanYuPinyinString(name, format, "\n", true);
		System.out.println("===========");
		System.out.println(s);
	}
}
