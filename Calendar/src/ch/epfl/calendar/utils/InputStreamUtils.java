package ch.epfl.calendar.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class provides tools to work with inputStreams
 * 
 * @author AblionGE
 * 
 */
public final class InputStreamUtils {

    private InputStreamUtils() {
    }

    public static String readInputStream(InputStream is, String encoding)
        throws IOException {
        if (is == null) {
            throw new IOException("The input stream is null");
        }
        if (encoding == null) {
            throw new IOException("The string to set the encoding is null");
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(is,
                encoding));
        StringBuilder sb = new StringBuilder();
        String read = in.readLine();
        while (read != null) {
            sb.append(read);
            read = in.readLine();
        }
        in.close();

        return sb.toString();
    }

}
