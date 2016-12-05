package org.loda.lalfred.util;

import java.io.FileNotFoundException;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public class Pinyin {

	static {
		try {
			PinyinHelper.addMutilPinyinDict("src/main/resources/user_multi_pinyin.dict");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String hanyuToPinyin(String str, String separator) {
		try {
			return PinyinHelper.convertToPinyinString(str, separator, PinyinFormat.WITHOUT_TONE);
		} catch (PinyinException e) {
			e.printStackTrace();
			return null;
		}
	}

}
