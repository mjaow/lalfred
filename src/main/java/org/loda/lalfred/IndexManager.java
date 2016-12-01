package org.loda.lalfred;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.LongAdder;

public class IndexManager implements Manager {

	private final File indexFile = new File("d://.lafred");

	private final ConcurrentMap<String, List<File>> indexes = new ConcurrentHashMap<>();

	private final LongAdder count = new LongAdder();

	private final BlockingQueue<File> queue = new LinkedBlockingQueue<>();

	private final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public IndexManager() {
		// if (!indexFile.exists()) {
		// try {
		// boolean r = indexFile.createNewFile();
		// if (!r) {
		// throw new RuntimeException("file not creat");
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }

		for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
			service.execute(() -> {
				getAndBuildIndex();
			});
		}
	}

	public void putToFilePool(File f) {
		try {
			queue.put(f);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<File> getByKey(String key) {
		List<File> files = indexes.get(key);
		return files == null ? Collections.emptyList() : files;
	}

	@Override
	public long getIndexCount() {
		return count.longValue();
	}

	public boolean checkIndexed(File f) {
		List<File> files = getByKey(f.getName());
		for (File indexFile : files) {
			if (f.equals(indexFile)) {
				return true;
			}
		}
		return false;
	}

	public ConcurrentMap<String, List<File>> showIndexes() {
		return indexes;
	}

	private void buildIndex(File f) {
		indexes.putIfAbsent(f.getName(), createEmptyList());
		indexes.get(f.getName()).add(f);
		count.increment();
	}

	private List<File> createEmptyList() {
		return new CopyOnWriteArrayList<>();
	}

	private void getAndBuildIndex() {
		while (true) {
			try {
				File f = queue.take();
				buildIndex(f);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
