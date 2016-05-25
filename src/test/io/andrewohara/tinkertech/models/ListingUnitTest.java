package io.andrewohara.tinkertech.models;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class ListingUnitTest {

	@Test
	public void testAdvancedEquipment() {
		Listing listing = TestLoader.loadListing("AdvancedEquipment");
		assertThat(listing.getName(), is("AdvancedEquipment"));
		
		Release latest = listing.getLatestRelease();
		assertThat(latest, notNullValue());
		assertThat(latest.getDownloadUrl(), nullValue());
		assertThat(latest.getMirrorUrl(), is("http://s3.amazonaws.com/factoriomods/mod_files/attachments/000/000/248/original/AdvancedEquipment_0.4.0.zip?1438488276"));
		assertThat(latest.getVersion(), equalTo(Version.valueOf("0.4.0")));
		assertThat(latest.getFilename(), is("AdvancedEquipment_0.4.0.zip"));
	}
}
