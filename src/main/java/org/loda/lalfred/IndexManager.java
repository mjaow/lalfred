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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.loda.lalfred.util.Assert;
import org.loda.lalfred.util.Pinyin;

import com.google.common.collect.Lists;

public class IndexManager implements Manager {

	private TST<File> fileTrie = new TST<>();

	private TST<File> contentTrie = new TST<>();

	private final LongAdder count = new LongAdder();

	private final BlockingQueue<File> buildIndexQ = new LinkedBlockingQueue<>();

	private final BlockingQueue<File> removeIndexQ = new LinkedBlockingQueue<>();

	private final ExecutorService buildIndexService = Executors.newSingleThreadExecutor();

	private final ExecutorService removeIndexService = Executors.newSingleThreadExecutor();

	private final Lock fileIndexLock = new ReentrantLock();

	private final Lock contentIndexLock = new ReentrantLock();

	// private final ExecutorService fileIndexService =
	// Executors.newSingleThreadExecutor();
	//
	// private final ExecutorService contentIndexService =
	// Executors.newSingleThreadExecutor();

	private final ExecutorService searchService = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public IndexManager() {

		buildIndexService.execute(() -> {
			getAndBuildIndex();
		});
		removeIndexService.execute(() -> {
			getAndRemoveIndex();
		});

	}

	private void getAndRemoveIndex() {
		while (true) {
			try {
				File f = removeIndexQ.take();
				removeIndex(FileTypeUtils.recognize(f));

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void removeIndex(Doc doc) {
		removeFileIndex(doc);
		removeContentIndex(doc);
		count.decrement();

	}

	private void removeContentIndex(Doc doc) {
		contentIndexLock.lock();
		try {
			removeIndex(contentTrie, doc, true);
		} finally {
			contentIndexLock.unlock();
		}
	}

	private void removeFileIndex(Doc doc) {
		fileIndexLock.lock();
		try {
			fileTrie.delete(doc.getFileName(), doc.getFile());
			removeIndex(fileTrie, doc, false);
		} finally {
			fileIndexLock.unlock();
		}
	}

	public void waitForBuildIndex(File f) {
		try {
			buildIndexQ.put(f);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void waitForRemoveIndex(File f) {
		try {
			removeIndexQ.put(f);
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
		fileIndexLock.lock();
		try {
			fileTrie.put(doc.getFileName(), doc.getFile());
			buildIndex(fileTrie, doc, false);
		} finally {
			fileIndexLock.unlock();
		}
	}

	private void buildContentIndex(Doc doc) {
		contentIndexLock.lock();
		try {
			buildIndex(contentTrie, doc, true);
		} finally {
			contentIndexLock.unlock();
		}
	}

	private void buildIndex(TST<File> trie, Doc doc, boolean fullText) {
		for (Token token : doc.getTokens(fullText)) {
			if (token.isChinese()) {
				String s = getPinyin(token.getText());
				trie.put(s, doc.getFile());
			}
			trie.put(token.getText(), doc.getFile());
		}
	}

	private void removeIndex(TST<File> trie, Doc doc, boolean fullText) {
		for (Token token : doc.getTokens(fullText)) {
			if (token.isChinese()) {
				String s = getPinyin(token.getText());
				trie.delete(s, doc.getFile());
			}
			trie.delete(token.getText(), doc.getFile());
		}
	}

	private String getPinyin(String text) {
		return Pinyin.hanyuToPinyin(text, "");
	}

	private void getAndBuildIndex() {
		while (true) {
			try {
				File f = buildIndexQ.take();
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
