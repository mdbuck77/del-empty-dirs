package com.mdbuck.delemptydirs;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public final class DelEmptyDirs {
	private final Path pwd;

	public DelEmptyDirs(Path pwd) {
		this.pwd = pwd;
	}

	public static void main(String[] args) {
		final Path root = Paths.get(args[0]);

		final Path pwd = Paths.get(System.getProperty("user.dir"));

		new DelEmptyDirs(pwd).delete(root);
	}

	private boolean delete(final Path root) {
		if (Files.isDirectory(root)) {

			try {
				if (Files.isSameFile(root, this.pwd)) {
					System.out.println("Unable to delete " + root.toAbsolutePath() + " because you are there.");
					return false;
				}
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}

			try (final Stream<Path> children = Files.list(root)) {
				final Boolean isEmpty = children.map(this::delete)
								.reduce((deleted, anotherDeleted) -> deleted & anotherDeleted)
								.orElse(true);
				if (isEmpty) {
					System.out.println("Deleting " + root);
					Files.delete(root);
					return true;
				}
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
		return false;
	}
}
