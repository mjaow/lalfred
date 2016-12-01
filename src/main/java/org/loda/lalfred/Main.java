package org.loda.lalfred;

import java.io.File;
import java.util.Scanner;

public class Main {

	private static final Manager manager = new IndexManager();

	// private static final Searcher searcher = new
	// ProducerConsumerSearcher(manager);
	private static final Searcher searcher = new FJSearcher(manager);

	public static void main(String[] args) {
		new Thread(() -> {
			File dir = new File("e:\\");
			long start = System.currentTimeMillis();
			searcher.search(dir, Integer.MAX_VALUE);
			long cost = System.currentTimeMillis() - start;
			System.out.println("cost " + cost + " ms");
		}).start();

		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if ("ct".equals(line)) {
				System.out.println(manager.getIndexCount());
			} else {
				System.out.println(manager.getByKey(line));
			}
		}
		scanner.close();

	}

}
