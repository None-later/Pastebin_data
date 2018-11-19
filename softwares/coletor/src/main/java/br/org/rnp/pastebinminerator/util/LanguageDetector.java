package br.org.rnp.pastebinminerator.util;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

public class LanguageDetector {
    private static char lastLanguageProfile;
    private static boolean profileLoaded;

    public synchronized static String getLanguage(String text) {
        String language;
        if (text.split(" ").length < 30) {
            language = detectShort(text);
        } else {
            language = detectLong(text);
        }
        return language;
    }

    private static String detectLong(String text) {
        if (lastLanguageProfile != 'l' || !profileLoaded) {
            DetectorFactory.clear();
            loadProfile(Util.LANGUAGE_PROFILE_PATH);
            lastLanguageProfile = 'l';
        }
        return detectLanguage(text);
    }

    private static String detectShort(String text) {
        if (lastLanguageProfile != 's' || !profileLoaded) {
            DetectorFactory.clear();
            loadProfile(Util.LANGUAGE_SHORT_PROFILE_PATH);
            lastLanguageProfile = 's';
        }
        return detectLanguage(text);
    }

    private static String detectLanguage(String text) {

        String lang = null;
        try {
            Detector detector = DetectorFactory.create();
            detector.append(text);
            lang = detector.detect();
        } catch (LangDetectException e) {
            // Log.log("Error detecting language using the profile at " + path +
            // "\n" + e.toString());
        }
        return lang;
    }

    /**
     * @param path
     */
    private static void loadProfile(String path) {
        try {
            DetectorFactory.loadProfile(path);
            profileLoaded = true;
        } catch (LangDetectException e) {
            Log.log("Error loading language profile at " + path + "\n" + e.toString());
        }
    }

}