package org.loda.lalfred;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class FJSearcher extends RecursiveTask<Void> implements Searcher {

	private static final long serialVersionUID = 1174720915223534283L;

	private List<File> dirs;

	private final Manager manager;

	private int depth;

	public FJSearcher(Manager manager) {
		this.manager = manager;
	}

	private FJSearcher(Manager manager, List<File> dirs) {
		this.manager = manager;
		this.dirs = dirs;
	}

	@Override
	public void search(File root, int depth) {
		this.depth = depth;
		ForkJoinPool pool = new ForkJoinPool();

		FJSearcher task = new FJSearcher(manager, toList(root.listFiles()));

		ForkJoinTask<Void> ft = pool.submit(task);

		try {
			ft.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}

	private List<File> toList(File[] files) {
		return files == null ? Collections.emptyList() : Arrays.asList(files);
	}

	@Override
	protected Void compute() {
		if (dirs == null || dirs.isEmpty()) {
			return null;
		}

		List<File> list = new ArrayList<>();
		for (File dir : dirs) {
			if (dir.isDirectory()) {
				list.add(dir);
			} else if (!manager.checkIndexed(dir)) {
				manager.putToFilePool(dir);
			}
		}

		if (list.size() < 5) {
			for (File dir : list) {
				doSearch(dir, depth);
			}
		} else {
			int mid = list.size() / 2;
			FJSearcher left = new FJSearcher(manager, list.subList(0, mid));
			FJSearcher right = new FJSearcher(manager, list.subList(mid + 1, list.size()));

			left.fork();
			right.fork();
			left.join();
			right.join();
		}

		return null;
	}

	private void doSearch(File dir, int depth) {
		File[] files = dir.listFiles();
		FJSearcher fj = new FJSearcher(manager, toList(files));
		fj.fork();
		fj.join();
	}

}
