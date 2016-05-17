package io.andrewohara.tinkertech.loaders;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import io.andrewohara.tinkertech.models.Mod;

public class ModLoader {

	private final Path dataPath;

	public ModLoader(Path dataPath) {
		this.dataPath = dataPath;
	}

	public Collection<Mod> listMods() {
		List<Mod> mods = new LinkedList<Mod>();

		return mods;
	}
}
