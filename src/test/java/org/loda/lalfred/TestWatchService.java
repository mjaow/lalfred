package org.loda.lalfred;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class TestWatchService {

	@Test
	public void watch() throws IOException, InterruptedException {
		final WatchService watch = FileSystems.getDefault().newWatchService();

		Path path = Paths.get("D:\\WEB-INF");

		path.register(watch, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);

		new Thread(() -> {
			while (true) {
				WatchKey key = null;
				try {
					key = watch.take();
					for (WatchEvent<?> event : key.pollEvents()) {
						Path p = (Path) event.context();
						System.out.println(p.getFileName());
						System.out.println(event.context() + "==>" + event.kind());
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					key.reset();
				}

			}
		}).start();

		Thread.sleep(10000);

//		path.register(watch,null,);

		new CountDownLatch(1).await();
	}
}
