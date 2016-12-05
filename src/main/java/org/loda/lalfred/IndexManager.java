package org.loda.lalfred;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.LongAdder;

import org.loda.lalfred.util.Assert;
import org.loda.lalfred.util.Pinyin;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class IndexManager implements Manager {

	private final File indexFile = new File("d://.lafred");

	private TST<File> trie = new TST<>();

	private final LongAdder count = new LongAdder();

	private final BlockingQueue<File> queue = new LinkedBlockingQueue<>();

	private final ExecutorService singleService = Executors.newSingleThreadExecutor();

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
		return trie.get(key);
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

	public void buildIndex(File f) {
		trie.put(f.getName(), f);

		for (Token token : getTokens(f.getName())) {
			if (token.isChinese()) {
				String s = getPinyin(token.getText());
				trie.put(s, f);
			}
			trie.put(token.getText(), f);
		}

		count.increment();

	}

	private String getPinyin(String text) {
		return Pinyin.hanyuToPinyin(text, "");
	}

	private List<Token> getTokens(String text) {
		List<Token> list = new ArrayList<>();
		IKSegmenter seg = new IKSegmenter(new StringReader(text), true);
		try {
			Lexeme lexeme;
			while ((lexeme = seg.next()) != null) {
				list.add(new Token(lexeme.getLexemeText(), lexeme.getLexemeType()));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		return list;
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
		return trie.valuesWithPrefix(prefix);
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
