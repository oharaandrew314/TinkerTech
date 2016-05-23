package io.andrewohara.tinkertech.mediators;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import io.andrewohara.tinkertech.models.Listing;
import io.andrewohara.tinkertech.models.Mod;

public interface Mediator {
	Stream<Listing> search(String query) throws IOException;
	InputStream download(Listing listing) throws IOException;
	Listing getListing(Mod mod);
}
