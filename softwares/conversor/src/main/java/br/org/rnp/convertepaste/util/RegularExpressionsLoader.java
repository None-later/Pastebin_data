package br.org.rnp.convertepaste.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import br.org.rnp.convertepaste.entity.RegularExpression;

/**
 * @author felipevr
 *
 *         Compilation of regular expressions used here.
 * 
 *         In the blacklist set, all the expressions marked with an * in the
 *         name are part of useful unknown perfect group but that helps a lot.
 *         The # are log expressions, generally.
 */
public class RegularExpressionsLoader {
    private static long acceptListDate = 0;
    private static long blackListDate = 0;
    // array of expressions
    private static List<RegularExpression> acceptedBy;
    private static List<RegularExpression> blackList;

    public static void loadRegularExpressions() {
        acceptedBy = loadRegularExpression(Util.acceptedBy_name);
        acceptListDate = Util.getFileDate(Util.acceptedBy_name);
        blackList = loadRegularExpression(Util.blackList_name);
        blackListDate = Util.getFileDate(Util.blackList_name);
    }

    /**
     * @param acceptedBy_name2
     * @return
     */
    private static List<RegularExpression> loadRegularExpression(String name) {
        List<RegularExpression> listJObj = createJsonFromFile(name);
        ArrayList<RegularExpression> failed = new ArrayList<RegularExpression>();
        for (RegularExpression r : listJObj) {
            try {
                r.compile();
            } catch (PatternSyntaxException e) {
                Log.log("Error loading expression: " + r.getName() + ": " + r.getExpressionText());
                failed.add(r);
            }
        }
        if (!failed.isEmpty()) {
            listJObj.removeAll(failed);
        }
        return listJObj;
    }

    /**
     * @param name
     * @return
     */
    private static List<RegularExpression> createJsonFromFile(String name) {
        StringBuilder sb = new StringBuilder();

        try {
            // FileReader fileReader = new FileReader(name);
            // BufferedReader bufferedReader = new BufferedReader(fileReader);
            // String line = bufferedReader.readLine();

            File file = new File(name);

            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));

            String line = in.readLine();

            while (line != null) {
                sb.append(line);
                line = in.readLine();
            }
            in.close();
        } catch (UnsupportedEncodingException e2) {
            Log.log("Error reading file " + name + ":\n" + e2.toString());
        } catch (IOException e) {
            Log.log("Error reading file: " + name + ":\n" + e.toString());
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<RegularExpression> listJObj = gson.fromJson(sb.toString(), new TypeToken<List<RegularExpression>>() {
        }.getType());
        return listJObj;
    }

    /**
     * @return the acceptedBy_name
     */
    public static String getAcceptedBy_name() {
        return Util.acceptedBy_name;
    }

    /**
     * @param acceptedBy_name
     *            the acceptedBy_name to set
     */
    public static void setAcceptedBy_name(String acceptedBy_name) {
        Util.acceptedBy_name = acceptedBy_name;
    }

    /**
     * @return the blackList_name
     */
    public static String getBlackList_name() {
        return Util.blackList_name;
    }

    /**
     * @param blackList_name
     *            the blackList_name to set
     */
    public static void setBlackList_name(String blackList_name) {
        Util.blackList_name = blackList_name;
    }

    /**
     * @return the acceptedBy
     */
    public static List<RegularExpression> getAcceptedBy() {
        if (acceptListDate < Util.getFileDate(Util.acceptedBy_name)) {
            Log.log("Loading regexes to acceptList");
            acceptedBy = loadRegularExpression(Util.acceptedBy_name);
            acceptListDate = Util.getFileDate(Util.acceptedBy_name);
        }
        return acceptedBy;
    }

    /**
     * @return the blackList
     */
    public static List<RegularExpression> getBlackList() {
        if (blackListDate < Util.getFileDate(Util.blackList_name)) {
            Log.log("Loading regexes to blackList");
            blackList = loadRegularExpression(Util.blackList_name);
            blackListDate = Util.getFileDate(Util.blackList_name);
        }
        return blackList;
    }
}