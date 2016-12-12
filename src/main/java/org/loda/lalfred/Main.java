package org.loda.lalfred;

import java.io.File;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class Main {

	/**
	 * pc : cost 2 ms ct 26 |cost 347 ms ct 14144 | cost 261793 ms 1253726
	 * 
	 * fj : cost 5 ms ct 26 |cost 432 ms ct 14144 | cost 263601 ms 1253674
	 */

	private static final Manager manager = new IndexManager();

	private static final Searcher searcher = new ProducerConsumerSearcher(manager);
	// private static final Searcher searcher = new FJSearcher(manager);

	private static final FileAlteration watcher = new FileAlteration(manager);

	public static void main(String[] args) {
		watcher.start();

		watcher.register("/Users/loda/Documents");

		load("/Users/loda/Documents");

		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if ("ct".equals(line)) {
				System.out.println(manager.getIndexCount());
			} else {
				long start = System.nanoTime();
				String[] tokens = StringUtils.split(line, " ");
				Set<File> list = manager.getByPrefix(tokens);

				long cost = System.nanoTime() - start;
				System.out.println("cost " + cost / 1000 + " us");
				System.out.println("size " + list.size());
				System.out.println(list);
			}
		}
		scanner.close();

		watcher.stop();
	}

	private static void load(String path) {
		File dir = new File(path);
		searcher.search(dir);
	}

}
