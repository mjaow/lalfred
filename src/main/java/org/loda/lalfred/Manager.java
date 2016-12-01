package org.loda.lalfred;

import java.io.File;
import java.util.List;

public interface Manager {

	public void putToFilePool(File f);

	public List<File> getByKey(String key);

	public long getIndexCount();

	public boolean checkIndexed(File f);
}
