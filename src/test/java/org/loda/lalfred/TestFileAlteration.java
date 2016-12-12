package org.loda.lalfred;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.loda.lalfred.util.Assert;
import org.loda.lalfred.util.Utils;

public class TestFileAlteration {

	@Test
	public void testFileAlteration() {

		FileAlteration controller = new FileAlteration(null);

		controller.register("/Users/loda/Documents");
		controller.register("/Users/loda/Movies");

		controller.start();

		Utils.await();
	}

	@Test
	public void testDirectory() {
		Path p = Paths.get("/Users/loda/Movies/test3");
		Assert.isTrue(p.toFile().isDirectory());
	}

	@Test
	public void testHiddenFile() {
		File f = new File("/Users/loda/Movies/.DS_Store");
		Assert.isTrue(f.isHidden());
		Assert.isTrue(f.isFile());
	}
}
