package io.andrewohara.tinkertech.models;

import static org.junit.Assert.*;

import org.junit.Test;

import io.andrewohara.tinkertech.version.Version;

import static org.hamcrest.CoreMatchers.*;

public class ModUnitTest {

	@Test
	public void airFiltering() {
		Mod mod = TestLoader.loadMod("air-filtering");
		assertThat(mod.getName(), is("air-filtering"));
		assertThat(mod.getTitle(), is("Air filtering"));
		assertThat(mod.getVersion(), equalTo(Version.valueOf("0.3.1")));
	}
}
