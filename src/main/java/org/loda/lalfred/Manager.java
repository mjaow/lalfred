package org.loda.lalfred;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public interface Manager {

	public void putToFilePool(File f);

	public List<File> getByKey(String key);

	public List<File> getByKey(String key, int limit);

	public List<File> getByPrefix(String prefix);

	public List<File> getByPrefix(String prefix, int limit);

	public long getIndexCount();

	public boolean checkIndexed(File f);

	public ConcurrentMap<String, List<File>> showIndexes();
}
