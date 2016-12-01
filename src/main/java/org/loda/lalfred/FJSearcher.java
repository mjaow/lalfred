//package org.loda.lalfred;
//
//import java.io.File;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ForkJoinPool;
//import java.util.concurrent.ForkJoinTask;
//import java.util.concurrent.RecursiveTask;
//
//public class FJSearcher implements Searcher {
//
//	private final Manager manager;
//
//	public FJSearcher(Manager manager) {
//		super();
//		this.manager = manager;
//	}
//
//	class SearcherTask extends RecursiveTask<Void> {
//
//		private static final long serialVersionUID = 1174720915223534283L;
//
//		private final File dir;
//
//		public SearcherTask(File dir) {
//			super();
//			this.dir = dir;
//		}
//
//		@Override
//		protected Void compute() {
//			if (dir == null) {
//				return null;
//			}
//
//			File[] dirs = dir.listFiles(f -> {
//				return !f.isHidden();
//			});
//			if (dirs == null) {
//				return null;
//			}
//
//			List<SearcherTask> tasks = new LinkedList<>();
//
//			for (File d : dirs) {
//				if (d.isDirectory()) {
//					tasks.add(new SearcherTask(d));
//				} else if (!manager.checkIndexed(d)) {
//					manager.putToFilePool(d);
//				}
//			}
//
//			for (SearcherTask task : tasks) {
//				task.fork();
//				task.join();
//			}
//
//			return null;
//		}
//
//	}
//
//	@Override
//	public void search(File... root) {
//		ForkJoinPool pool = new ForkJoinPool();
//
//		SearcherTask task = new SearcherTask(root);
//
//		ForkJoinTask<Void> ft = pool.submit(task);
//
//		try {
//			ft.get();
//		} catch (InterruptedException | ExecutionException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//}
