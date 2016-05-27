package io.andrewohara.tinkertech;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import io.andrewohara.tinkertech.os.LinuxContext;
import io.andrewohara.tinkertech.os.OSContext;
import io.andrewohara.tinkertech.os.MacContext;
import io.andrewohara.tinkertech.os.WindowsContext;

public class OSModule extends AbstractModule {

	private final OSContext osContext;

	public static OSModule create() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return new OSModule(new WindowsContext());
		} else if (os.contains("mac")) {
			return new OSModule(new MacContext());
		} else if (os.contains("nix") || os.contains("nux")) {
			return new OSModule(new LinuxContext());
		}
		throw new IllegalStateException("OS not supported: " + os);
	}

	public OSModule(OSContext osContext) {
		if (osContext == null) {
			throw new IllegalArgumentException("OSContextn n n n n n n n n n n n n n n n n n n n n n n n n b cannot be null");
		}
		this.osContext = osContext;
	}

	@Override
	protected void configure() {
	}

	@Provides
	public OSContext provideOSContext() {
		return osContext;
	}
}
