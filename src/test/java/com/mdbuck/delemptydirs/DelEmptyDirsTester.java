package com.mdbuck.delemptydirs;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public final class DelEmptyDirsTester {

	@Test
	public void emptyDir() throws IOException {
		final Path root = Files.createTempDirectory("DelEmptyDirsTester-emptyDir-");
		DelEmptyDirs.main(new String[] {root.toString()});

		assertThat(Files.exists(root)).isFalse();
	}

	@Test
	public void dirWithOneFile() throws IOException {
		final Path root = Files.createTempDirectory("DelEmptyDirsTester-dirWithOneFile-");

		final Path tempFile = Files.createTempFile(root, "-afile", "");

		DelEmptyDirs.main(new String[] {root.toString()});

		assertThat(Files.exists(root)).isTrue();

		Files.delete(tempFile);
		Files.delete(root);
	}

	@Test
	public void dirWithOneEmptyDir() throws IOException {
		final Path root = Files.createTempDirectory("DelEmptyDirsTester-dirWithOneEmptyDir-");

		final Path childDir = Files.createTempDirectory(root, "child");

		DelEmptyDirs.main(new String[] {root.toString()});

		assertThat(Files.exists(root)).isFalse();
		assertThat(Files.exists(childDir)).isFalse();
	}

	@Test
	public void dirWithOneDirWithAFile() throws IOException {
		final Path root = Files.createTempDirectory("DelEmptyDirsTester-dirWithOneDirWithAFile-");

		final Path childDir = Files.createTempDirectory(root, "child");
		final Path tempFile = Files.createTempFile(childDir, "-afile", "");

		DelEmptyDirs.main(new String[] {root.toString()});

		assertThat(Files.exists(root)).isTrue();

		Files.delete(tempFile);
		Files.delete(childDir);
		Files.delete(root);
	}

	/**
	 * root/child1 should not be deleted because root/child1 contains a file
	 * root/child2 should be deleted because it does not contain anything
	 */
	@Test
	public void dir() throws IOException {
		final Path root = Files.createTempDirectory("DelEmptyDirsTester-dir-");

		final Path child1 = Files.createTempDirectory(root, "-child1-");
		final Path childFile1 = Files.createTempFile(child1, "-child1-", "");

		final Path child2 = Files.createTempDirectory(root, "-child2-");

		DelEmptyDirs.main(new String[] {root.toString()});

		assertThat(Files.exists(root)).isTrue();
		assertThat(Files.exists(child1)).isTrue();
		assertThat(Files.exists(childFile1)).isTrue();

		assertThat(Files.exists(child2)).isFalse();
	}

}
