package io.andrewohara.tinkertech.version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionParser {

	private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+)(\\.|-)(\\d+)((\\.|-)\\d+)*");

	public static String parseVersionString(String string){
		if (string == null){
			return null;
		}

		Matcher m = VERSION_PATTERN.matcher(string);
		if (m.find()){
			return m.group().replace("-", ".");
		}
		return null;
	}

}
