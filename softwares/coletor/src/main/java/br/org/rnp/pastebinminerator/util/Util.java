
package br.org.rnp.pastebinminerator.util;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * @author felipevr
 *
 *         Set of usefull methods and constants.
 */
public class Util {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static final String KEY_WORDS_ACCEPT_FILE = "resources/keywords/accept.txt";
    public static final String KEY_WORDS_BLACKLIST_FILE = "resources/keywords/reject.txt";
    public static final String LANGUAGE_PROFILE_PATH = "resources/profiles";
    public static final String LANGUAGE_SHORT_PROFILE_PATH = "resources/profiles.sm";
    public static String acceptedBy_name = "resources/regex/acceptedby.json";
    public static String blackList_name = "resources/regex/blacklist.json";
    public static final String PREFIX_REGEX = "re_";
    public static final String PREFIX_KEYWORD = "kw_";
    public static final String PREFIX_LANGUAGE = "lg_";

    public static long getFileDate(String name) {
        File file = new File(name);
        return file.lastModified();
            }
}