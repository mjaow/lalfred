package org.loda.lalfred;

import java.io.FileNotFoundException;

import org.junit.Test;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public class TestPinyin {

	@Test
	public void testPy() throws PinyinException, FileNotFoundException {
		String name = "长者 你好";
//		PinyinHelper.addMutilPinyinDict("src/main/resources/user_multi_pinyin.dict");
		String s = PinyinHelper.convertToPinyinString(name, "\n", PinyinFormat.WITHOUT_TONE);
		System.out.println(s);
	}
}
