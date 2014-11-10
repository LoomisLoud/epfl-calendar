package ch.epfl.calendar.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * This class provides tools to work with inputStreams
 * @author AblionGE
 *
 */
public final class InputStreamUtils {
    
    private static final int BUF_SIZE = 1024;
    
    public static String readInputStream(InputStream is) throws IOException {
        Reader in = new InputStreamReader(is, "ISO-8859-1");
        char[] buffer = new char[BUF_SIZE];
        int read = in.read(buffer);
        StringBuilder sb = new StringBuilder();

        while (read > 0) {
            sb.append(String.copyValueOf(buffer, 0, read));
            buffer = new char[BUF_SIZE];
            read = in.read(buffer);
        }
        in.close();

        return sb.toString();
    }

}
