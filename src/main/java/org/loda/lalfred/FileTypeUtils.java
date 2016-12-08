package org.loda.lalfred;

import java.io.File;

public class FileTypeUtils {

	public static Doc recognize(File f) {
		return new Doc(f);
	}

}
