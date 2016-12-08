package org.loda.lalfred;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.LongAdder;

import org.loda.lalfred.util.Assert;
import org.loda.lalfred.util.Pinyin;

import com.google.common.collect.Lists;

public class IndexManager implements Manager {

	// private final File indexFile = new File("d://.lafred");

	private TST<File> fileTrie = new TST<>();

	private TST<File> contentTrie = new TST<>();

	private final LongAdder count = new LongAdder();

	private final BlockingQueue<File> queue = new LinkedBlockingQueue<>();

	private final ExecutorService buildIndexService = Executors.newSingleThreadExecutor();

	// private final ExecutorService fileIndexService =
	// Executors.newSingleThreadExecutor();
	//
	// private final ExecutorService contentIndexService =
	// Executors.newSingleThreadExecutor();

	private final ExecutorService searchService = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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

		buildIndexService.execute(() -> {
			getAndBuildIndex();
		});
	}

	public void putToFilePool(File f) {
		try {
			queue.put(f);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getIndexCount() {
		return count.longValue();
	}

	public boolean checkIndexed(File f) {
		for (File indexFile : fileTrie.get(f.getName())) {
			if (f.equals(indexFile)) {
				return true;
			}
		}
		return false;
	}

	public void buildIndex(Doc doc) {
		buildFileIndex(doc);
		buildContentIndex(doc);

		count.increment();

	}

	private void buildFileIndex(Doc doc) {
		fileTrie.put(doc.getFileName(), doc.getFile());
		buildIndex(fileTrie, doc, false);
	}

	private void buildContentIndex(Doc doc) {
		buildIndex(contentTrie, doc, true);
	}

	private void buildIndex(TST<File> trie, Doc doc, boolean fullText) {
		for (Token token : doc.getTokens(fullText)) {
			if (token.isChinese()) {
				String s = getPinyin(token.getText());
				fileTrie.put(s, doc.getFile());
			}
			fileTrie.put(token.getText(), doc.getFile());
		}
	}

	private String getPinyin(String text) {
		return Pinyin.hanyuToPinyin(text, "");
	}

	private void getAndBuildIndex() {
		while (true) {
			try {
				File f = queue.take();
				buildIndex(FileTypeUtils.recognize(f));

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private <T> Set<T> filterSet(Set<T> list, int limit) {
		Assert.isNoNegative(limit);
		if (limit == 0 || list == null || list.isEmpty()) {
			return Collections.emptySet();
		}

		Set<T> newList = new HashSet<>();

		int i = 0;
		for (T t : list) {
			if (i >= limit) {
				break;
			}
			newList.add(t);
			i++;
		}

		return newList;
	}

	@Override
	public Set<File> getByPrefix(String... prefixes) {
		if (prefixes == null || prefixes.length == 0) {
			return Collections.emptySet();
		}
		if (prefixes.length == 1) {
			return fileTrie.valuesWithPrefix(prefixes[0]);
		}

		final Map<File, Integer> countMap = new HashMap<>();

		if (prefixes.length > 5) {
			List<CompletableFuture<Set<File>>> futures = Lists.newArrayListWithCapacity(prefixes.length);
			for (String prefix : prefixes) {
				futures.add(CompletableFuture.supplyAsync(() -> {
					return fileTrie.valuesWithPrefix(prefix);
				}, searchService));
			}

			for (CompletableFuture<Set<File>> future : futures) {
				try {
					for (File f : future.get()) {
						Integer ct = countMap.get(f);
						if (ct == null) {
							ct = 0;
						}
						ct++;
						countMap.put(f, ct);
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		} else {
			for (String prefix : prefixes) {
				for (File f : fileTrie.valuesWithPrefix(prefix)) {
					Integer ct = countMap.get(f);
					if (ct == null) {
						ct = 0;
					}
					ct++;
					countMap.put(f, ct);
				}
			}
		}

		Set<File> set = new HashSet<>();
		for (Map.Entry<File, Integer> element : countMap.entrySet()) {
			if (element.getValue() == prefixes.length) {
				set.add(element.getKey());
			}
		}
		return set;
	}

	@Override
	public Set<File> getByPrefix(int limit, String... prefixes) {
		return filterSet(getByPrefix(prefixes), limit);
	}

}
