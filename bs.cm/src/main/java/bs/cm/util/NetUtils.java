package bs.cm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class NetUtils {

	public static String getStrFromReader(BufferedReader reader) {

		StringBuilder builder = new StringBuilder();
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

}
