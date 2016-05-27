package io.andrewohara.tinkertech.loaders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.google.inject.Inject;

import io.andrewohara.tinkertech.config.Config;
import io.andrewohara.tinkertech.models.Mod;
import io.andrewohara.tinkertech.views.ErrorHandler;

public class ModLoader {

	private final ErrorHandler errorHandler;
	private final Config config;

	@Inject
	protected ModLoader(ErrorHandler errorHandler, Config config) {
		this.errorHandler = errorHandler;
		this.config = config;
	}

	public Stream<Mod> listMods() throws IOException {
		try {
			return Files
					.list(config.getModsPath())
					.filter(path -> !Files.isDirectory(path))
					.filter(path -> path.getFileName().toString().endsWith(".zip"))
					.map(path -> loadMod(path))
					.filter(mod -> mod != null);
		} catch(NoSuchFileException e) {
			errorHandler.handleError(e);
			return Stream.empty();
		}
	}

	private Mod loadMod(Path modZipPath) {
		String infoPath = String.format("%s/info.json", FilenameUtils.getBaseName(modZipPath.toString()));

		try (ZipFile zipFile = new ZipFile(modZipPath.toFile())) {
			ZipEntry entry = zipFile.getEntry(infoPath);
			if (entry == null) {
				String message = modZipPath.getFileName().toString() + " is not compliant with Factorio Modding standards";
				errorHandler.handleError(new IOException(message));
				return null;
			}
			String json = IOUtils.toString(zipFile.getInputStream(entry));
			return new Mod(new JSONObject(json), modZipPath);
		} catch (IOException e) {
			String message = "There was an error scanning the mod: " + modZipPath.getFileName().toString();
			errorHandler.handleError(new IOException(message, e));
			return null;
		}
	}
}
