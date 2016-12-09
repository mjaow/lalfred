package org.loda.lalfred;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WatchServiceController {

	private WatchService watcher;

	private ExecutorService service;

	public WatchServiceController() {
		try {
			watcher = FileSystems.getDefault().newWatchService();
			service = Executors.newSingleThreadExecutor();
			service.execute(() -> {

				while (true) {
					try {
						WatchKey key = watcher.take();
						for (WatchEvent<?> event : key.pollEvents()) {
							Path p = (Path) event.context();
							File file = p.toFile();
							Kind<?> kind = event.kind();
							switch (kind.name()) {
							case "ENTRY_CREATE":
								if (file.isDirectory()) {
									register(p);
								}
								break;
							case "ENTRY_DELETE":
								if (file.isDirectory()) {
									unregister(p);
								}
								break;

							default:
								break;
							}

						}
						key.reset();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void register(Path path) {
		try {
			path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY,
					StandardWatchEventKinds.ENTRY_DELETE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void unregister(Path path) {
		try {
			path.register(watcher);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
