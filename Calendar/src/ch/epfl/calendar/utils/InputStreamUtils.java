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

    /**
     * Reads an input stream and returns a String containing the content of the {@link InputStream}
     * @param is the {@link InputStream} to read
     * @param encoding the encoding of the bytes in the {@link InputStream}
     * @return a String containing the content of the {@link InputStream}
     * @throws IOException
     */
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
