package io.andrewohara.tinkertech.os;

import java.nio.file.Path;
import java.util.Optional;

public interface OSContext {

	Path getStoragePath();
	Optional<Path> getDataPath();
}
