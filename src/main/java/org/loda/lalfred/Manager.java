package org.loda.lalfred;

import java.io.File;
import java.util.Set;

public interface Manager {

	public void waitForBuildIndex(File f);

	public void waitForRemoveIndex(File f);

	public Set<File> getByPrefix(String... prefixs);

	public Set<File> getByPrefix(int limit, String... prefixs);

	public long getIndexCount();

	public boolean checkIndexed(File f);

}
