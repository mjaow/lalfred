package org.loda.lalfred;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {

	/**
	 * pc : cost 2 ms ct 26 |cost 347 ms ct 14144 | cost 261793 ms 1253726
	 * 
	 * fj : cost 5 ms ct 26 |cost 432 ms ct 14144 | cost 263601 ms 1253674
	 */

	private static final Manager manager = new IndexManager();

	private static final Searcher searcher = new ProducerConsumerSearcher(manager);
	// private static final Searcher searcher = new FJSearcher(manager);

	public static void main(String[] args) {

		load("/Users/loda/Movies/movie");

		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if ("ct".equals(line)) {
				System.out.println(manager.getIndexCount());
			} else {
				List<File> list = manager.getByPrefix(line, 10);
				System.out.println("size " + list.size());
				System.out.println(list);
			}
		}
		scanner.close();

	}

	private static void load(String path) {
		File dir = new File(path);
		searcher.search(dir);
	}

}
