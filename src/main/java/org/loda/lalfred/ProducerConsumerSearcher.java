package org.loda.lalfred;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class ProducerConsumerSearcher implements Searcher {

	private final Manager manager;

	public ProducerConsumerSearcher(Manager manager) {
		this.manager = manager;
	}

	@Override
	public void search(File... bases) {
		@SuppressWarnings("unchecked")
		CompletableFuture<Void>[] futures = new CompletableFuture[bases.length];
		for (int i = 0; i < futures.length; i++) {
			final File dir = bases[i];
			futures[i] = CompletableFuture.runAsync(() -> {
				searchRecursive(dir);
			});
		}

		final long start = System.currentTimeMillis();
		CompletableFuture.allOf(futures).thenRun(() -> {
			long cost = System.currentTimeMillis() - start;
			System.out.println("finished!cost " + cost + " ms");
		});

	}

	private void searchRecursive(File root) {
		File[] files = root.listFiles(f -> {
			return !f.isHidden();
		});

		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					searchRecursive(f);
				} else if (!manager.checkIndexed(f)) {
					manager.waitForBuildIndex(f);
				}
			}
		}
	}

}
