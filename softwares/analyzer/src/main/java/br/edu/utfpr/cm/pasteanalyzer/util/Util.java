package br.edu.utfpr.cm.pasteanalyzer.util;

import java.io.File;
import java.text.SimpleDateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author felipevr
 *
 *         Set of usefull methods and constants.
 */
public class Util {
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final int LENGTH_TO_SHOW = 20 * 40; // 2- lines, 80 collumns
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static final String FILE_FILTEREDIDS = "filteredids.json";
    public static final String FILTER_FILE = "filter.txt";

    public static long getFileDate(String name) {
        File file = new File(name);
        return file.lastModified();
    }
}