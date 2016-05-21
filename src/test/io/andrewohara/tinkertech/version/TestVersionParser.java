package io.andrewohara.tinkertech.version;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestVersionParser {

	private void check(String expected, String filename){
		assertEquals(expected, VersionParser.parseVersionString(filename));
	}

	@Test
	public void testNull(){
		check(null, null);
	}

	@Test
	public void testModuleManager() {
		check("2.5.9", "ModuleManager.2.5.9.dll");
	}

	@Test
	public void test4DigitVersion1(){
		check("1.0.15.2", "KerbalEngineer-1.0.15.2.zip");
	}

	@Test
	public void test4DigitVersion2(){
		check("0.6.2.4", "Engineer_Redux_v0.6.2.4.zip");
	}

	@Test
	public void testKWRocketry(){
		check("2.6", "KW Rocketry 2.6d2.zip");
	}

	@Test
	public void testEve(){
		check("7.4", "EnvironmentalVisualEnhancements-7-4-LR.zip");
	}

	@Test
	public void testAutoAsparagus(){
		check("0.8", "AutoAsparagus v0.8.zip");
	}
}

