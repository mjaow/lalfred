package org.loda.lalfred;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class FileAlteration {

	private final FileAlterationMonitor monitor;

	private final Manager manager;

	public FileAlteration(Manager manager) {
		monitor = new FileAlterationMonitor(5000);
		this.manager = manager;
		start();
	}

	public void start() {
		try {
			monitor.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void register(File file) {
		FileAlterationObserver observer = new FileAlterationObserver(file);
		observer.addListener(new FileAlterationListenerAdaptor() {

			@Override
			public void onDirectoryCreate(File directory) {
				if (isExpectedDir(directory)) {
					System.out.println("目录" + directory + "创建");
				}
			}

			private boolean isExpectedDir(File file) {
				return !file.getPath().contains("Windows 7.vmwarevm");
			}

			@Override
			public void onDirectoryDelete(File directory) {
				if (isExpectedDir(directory)) {
					System.out.println("目录" + directory + "删除");
				}
			}

			@Override
			public void onFileCreate(File file) {
				if (isExpected(file)) {
					manager.putToFilePool(file);
				}
			}

			@Override
			public void onFileChange(File file) {
				if (isExpected(file)) {
					System.out.println("文件" + file + "修改");
				}
			}

			@Override
			public void onFileDelete(File file) {
				if (isExpected(file)) {
					System.out.println("文件" + file + "删除");
				}
			}

			private boolean isExpected(File file) {
				return file.isFile() && !file.isHidden() && !file.getPath().contains("Windows 7.vmwarevm");
			}

		});
		monitor.addObserver(observer);
	}

	public void stop() {
		try {
			monitor.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
