package br.org.rnp.pastebinminerator.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class KeyWordsLoader {
    private static Set<String> acceptList;
    private static Set<String> blackList;

    private static long blackListDate = 0L;
    private static long acceptListDate = 0L;

    public static void loadKeyWords() {
        acceptList = loadKeyWordsFromFile(Util.KEY_WORDS_ACCEPT_FILE);
        acceptListDate = Util.getFileDate(Util.KEY_WORDS_ACCEPT_FILE);
        blackList = loadKeyWordsFromFile(Util.KEY_WORDS_BLACKLIST_FILE);
        blackListDate = Util.getFileDate(Util.KEY_WORDS_BLACKLIST_FILE);
    }

    private static Set<String> loadKeyWordsFromFile(String name) {
        String fileContent = readFile(name).replace("\n", "");
        String[] contentList = fileContent.split(",");
        Set<String> keywords = new HashSet<String>();
        for (String s : contentList) {
            if (s != null && !s.isEmpty()) {
                keywords.add(s.trim());
            }
        }
        return keywords;
    }

    /**
     * @param name
     *            the file's name
     * @return the file's content.
     * @throws IOException
     *             if any error happens while reading the file
     */
    private static String readFile(String name) {
        StringBuilder sb = new StringBuilder();

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                sb.append(line);
                line = bufferedReader.readLine();
            }
            fileReader.close();
        } catch (IOException e) {
            Log.log("Error reading file: " + name, Log.class.getName());
        }
        return sb.toString();
    }

    /**
     * @return the acceptList
     */
    public static Set<String> getAcceptList() {
        if (acceptListDate < Util.getFileDate(Util.KEY_WORDS_ACCEPT_FILE)) {
            Log.log("Loading acceptlist keywords");
            acceptList = loadKeyWordsFromFile(Util.KEY_WORDS_ACCEPT_FILE);
            acceptListDate = Util.getFileDate(Util.KEY_WORDS_ACCEPT_FILE);
        }
        return acceptList;
    }

    /**
     * @return the blackList
     */
    public static Set<String> getBlackList() {
        if (blackListDate < Util.getFileDate(Util.KEY_WORDS_BLACKLIST_FILE)) {
            Log.log("Loading blacklist keywords");
            blackList = loadKeyWordsFromFile(Util.KEY_WORDS_BLACKLIST_FILE);
            blackListDate = Util.getFileDate(Util.KEY_WORDS_BLACKLIST_FILE);
        }
        return blackList;
    }
}