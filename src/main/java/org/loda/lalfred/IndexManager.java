package org.loda.lalfred;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.LongAdder;

import org.apache.commons.lang3.StringUtils;
import org.loda.lalfred.util.Assert;
import org.loda.lalfred.util.Pinyin;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class IndexManager implements Manager {

	private final File indexFile = new File("d://.lafred");

	private final ConcurrentMap<String, List<File>> indexes = new ConcurrentHashMap<>();

	private TST<String> trie = new TST<>();

	private final LongAdder count = new LongAdder();

	private final BlockingQueue<File> queue = new LinkedBlockingQueue<>();

	private final ExecutorService singleService = Executors.newSingleThreadExecutor();

	private static final String PINYIN_SEPARATOR = "\n";

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

		singleService.execute(() -> {
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
	public List<File> getByKey(String key) {
		String s = trie.get(key);
		if (s == null) {
			return Collections.emptyList();
		}
		List<File> files = indexes.get(s);
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

	public void buildIndex(File f) {
		indexes.putIfAbsent(f.getName(), createEmptyList());
		indexes.get(f.getName()).add(f);
		trie.put(f.getName(), f.getName());

		for (String text : getTokens(f.getName())) {
			String[] arr = StringUtils.split(getPinyin(text), PINYIN_SEPARATOR);

			for (String s : arr) {
				trie.put(s, f.getName());
			}
		}

		count.increment();
	}

	private String getPinyin(String text) {
		return Pinyin.hanyuToPinyin(text, PINYIN_SEPARATOR);
	}

	private List<String> getTokens(String text) {
		List<String> list = new ArrayList<>();
		IKSegmenter seg = new IKSegmenter(new StringReader(text), true);
		try {
			Lexeme lexeme;
			while ((lexeme = seg.next()) != null) {
				list.add(lexeme.getLexemeText());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		return list;
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

	@Override
	public List<File> getByPrefix(String prefix) {
		List<String> list = trie.valuesWithPrefix(prefix);
		List<File> search = new ArrayList<>();
		for (String key : list) {
			List<File> fs = indexes.get(key);
			if (fs != null) {
				search.addAll(fs);
			}
		}

		return search;
	}

	private <T> List<T> filterList(List<T> list, int limit) {
		Assert.isNoNegative(limit);
		if (limit == 0) {
			return Collections.emptyList();
		}

		List<T> newList = new ArrayList<>();
		for (int i = 0; i < limit && i < list.size(); i++) {
			newList.add(list.get(i));
		}

		return newList;
	}

	@Override
	public List<File> getByKey(String key, int limit) {
		return filterList(getByKey(key), limit);
	}

	@Override
	public List<File> getByPrefix(String prefix, int limit) {
		return filterList(getByPrefix(prefix), limit);
	}

}
