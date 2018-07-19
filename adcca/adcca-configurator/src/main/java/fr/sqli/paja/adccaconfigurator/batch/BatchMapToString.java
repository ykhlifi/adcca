package fr.sqli.paja.adccaconfigurator.batch;

import java.util.Map;

/**
 * 
 * @author sebouyahmed
 *
 */
public class BatchMapToString {
	/**
	 * readMap() transformer Map to String
	 * @param map
	 * @return String
	 */
	public static String readMap(Map<String, String> map) {

		final StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, String> entry : map.entrySet()) {

			sb.append(entry.getKey() + "," + entry.getValue() + "\n");

		}
		return sb.toString();
	}

}
