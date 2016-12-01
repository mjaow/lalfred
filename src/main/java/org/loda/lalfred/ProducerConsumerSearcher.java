package org.loda.lalfred;

import java.io.File;

public class ProducerConsumerSearcher implements Searcher {

	private final Manager manager;

	public ProducerConsumerSearcher(Manager manager) {
		this.manager = manager;
	}

	@Override
	public void search(File dir, int depth) {
		if (depth == 0) {
			return;
		}
		File[] files = dir.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					search(f, depth - 1);
				} else if (!manager.checkIndexed(f)) {
					manager.putToFilePool(f);
				}
			}
		}
	}

}
