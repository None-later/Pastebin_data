package br.org.rnp.convertepaste;

import java.util.ArrayList;
import java.util.HashMap;

import br.org.rnp.convertepaste.dao.temporary.PasteKeyTemporarySetDAO;
import br.org.rnp.convertepaste.minner.DataManage;
import br.org.rnp.convertepaste.util.KeyWordsLoader;
import br.org.rnp.convertepaste.util.Log;
import br.org.rnp.convertepaste.util.RegularExpressionsLoader;

public class Main {
    public static void main(String[] args) {
        Log.log("Initializing system...");
        HashMap<String, String> input = parseInput(args);
        int cacheLength = Integer.parseInt(input.get("cl")), minimumCacheLength = Integer.parseInt(input.get("mcl"));
        int numberOfPastes = Integer.parseInt(input.get("p")), numberOfSeconds = Integer.parseInt(input.get("s"));
        Log.setFileName(input.get("lf"));
        Log.setMaxLength(Integer.parseInt(input.get("ll")));
        Log.setLogToFile(Boolean.valueOf(input.get("ltf")));
        Log.clearLogFile();
        Log.log("Loading regular expressions...");
        RegularExpressionsLoader.loadRegularExpressions();
        Log.log("loading key words...");
        KeyWordsLoader.loadKeyWords();
        Log.log("Loaded: acept list length = " + KeyWordsLoader.getAcceptList().size() + ", blacklist length = "
                + KeyWordsLoader.getBlackList().size() + ".");
        Log.log("Setting languages...");
        // ArrayList<String> languages = parseLanguages(input.get("lang"));
        ArrayList<String> languages = new ArrayList<String>();
        languages.add("pt");
        languages.add("en");
        Log.log("Accept-languages: " + languages.toString());
        Log.log("Setting cacheLength = " + cacheLength + " and minimumCacheLength = " + minimumCacheLength + ".");
        PasteKeyTemporarySetDAO.setMaximumCacheLength(cacheLength);
        PasteKeyTemporarySetDAO.setMinimumCacheLength(minimumCacheLength);
        Log.log("Preparing execution: " + numberOfPastes + " pastes each " + numberOfSeconds + " seconds. ");
        DataManage dataManage = new DataManage(numberOfPastes, languages);
        Log.log("Starting execution.");
        dataManage.run();
    }

    private static ArrayList<String> parseLanguages(String langs) {
        ArrayList<String> languages = new ArrayList<String>();
        for (String lang : langs.split(",")) {
            if (lang != null && !lang.isEmpty()) {
                languages.add(lang.trim());
            }
        }
        return languages;
    }

    private static HashMap<String, String> parseInput(String[] args) {
        HashMap<String, String> input = new HashMap<String, String>();

        for (String arg : args) {
            if (arg.startsWith("-cl")) {
                input.put("cl", arg.replace("-cl", ""));
            } else if (arg.startsWith("-mcl")) {
                input.put("mcl", arg.replace("-cl", ""));
            } else if (arg.startsWith("-lf")) {
                input.put("lf", arg.replace("-lf", ""));
            } else if (arg.startsWith("-ll")) {
                input.put("ll", arg.replace("-ll", ""));
            } else if (arg.startsWith("-p")) {
                input.put("p", arg.replace("-p", ""));
            } else if (arg.startsWith("-s")) {
                input.put("s", arg.replace("-s", ""));
            } else if (arg.startsWith("-ltf")) {
                input.put("ltf", arg.replace("-ltf", ""));
            } else if (arg.startsWith("-lang")) {
                input.put("lang", arg.replace("-lang", ""));
            } else {
                System.out.println("Opção inválida: " + arg);
            }
        }

        assignDefaultValues(input);
        return input;
    }

    private static void assignDefaultValues(HashMap<String, String> input) {
        if (!input.containsKey("cl")) {
            input.put("cl", "1000");
        }
        if (!input.containsKey("mcl")) {
            input.put("mcl", "600");
        }
        if (!input.containsKey("lf")) {
            input.put("lf", "log.txt");
        }
        if (!input.containsKey("ll")) {
            input.put("ll", "2048");
        }
        if (!input.containsKey("p")) {
            input.put("p", "100");
        }
        if (!input.containsKey("s")) {
            input.put("s", "60");
        }
        if (!input.containsKey("ltf")) {
            input.put("ltf", "false");
        }
        if (!input.containsKey("lang")) {
            input.put("lang", "");
        }
    }
}