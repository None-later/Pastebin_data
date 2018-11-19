package br.edu.utfpr.cm.pasteanalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import br.edu.utfpr.cm.pasteanalyzer.dao.persistent.PasteJpaDAO;
import br.edu.utfpr.cm.pasteanalyzer.entity.jpa.Paste;
import br.edu.utfpr.cm.pasteanalyzer.entity.jpa.PasteEntity;
import br.edu.utfpr.cm.pasteanalyzer.entity.jpa.Reason;

public class Main {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private static final String ext = ".json";

    // dir variables
    private static final String baseDir = "pastes";
    private static final String entityDir = baseDir + "\\entity";
    private static final String reasonDir = baseDir + "\\reason";
    private static final String pasteDir = baseDir + "\\paste";
    private static Map<String, Reason> mapReasonsFirst;

    public static void main(String[] args) throws Exception {
        mapReasonsFirst = new HashMap<String, Reason>();
        createDirectories();

        List<Paste> pastes = PasteJpaDAO.getInstance().getAll();
        List<String> listReasons = new ArrayList<String>();

        readReasonDir(listReasons);
        for (Paste p : pastes) {
            saveReasons(listReasons, p);
            saveEntities(p);
            savePaste(p);
        }
    }

    private static void createDirectories() {
        new File(baseDir).mkdir();
        new File(entityDir).mkdir();
        new File(reasonDir).mkdir();
        new File(pasteDir).mkdir();
    }

    private static void readReasonDir(List<String> reasons) {
        System.out.println("Lendo diretório de reasons...");
        File dir = new File(reasonDir);
        String[] files = dir.list();
        for (String f : files) {
            if (!f.startsWith("re_")) {
                continue;
            }
            try {
                Reason r = gson.fromJson(new FileReader(reasonDir + "\\" + f), Reason.class);
                mapReasonsFirst.put(r.getReason(), r);
            } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
                System.out.println("Erro carregando JSON de reasons: " + e.toString());
            }
        }
        System.out.println("Reasons carregadas.");
    }

    private static void savePaste(Paste p) {
        String fileName = pasteDir + "\\" + (p.isRelevant() ? "r_" : "i_") + p.getKey() + ext;
        String fileData = gson.toJson(p);
        writeToFile(fileName, fileData);
    }

    private static void saveEntities(Paste p) {
        HashMap<String, Integer> mapReasons = new HashMap<String, Integer>();
        Iterator<PasteEntity> iterator = p.getEntities().iterator();
        while (iterator.hasNext()) {
            PasteEntity e = iterator.next();
            String r = e.getReason().getReason().replace("*", "ast");
            if (r.startsWith("re_") && !mapReasons.containsKey(r)) {
                mapReasons.put(r, 1);
            }
            e.setjSonReason(r);
            String fileName = entityDir + "\\" + p.getKey() + "-" + r + "-" + mapReasons.get(r) + ext;
            mapReasons.put(r, mapReasons.get(r) + 1);
            String fileData = gson.toJson(e);
            writeToFile(fileName, fileData);
        }
    }

    private static void saveReasons(List<String> listReasons, Paste paste) {
        Iterator<Reason> iterator = paste.getReasons().iterator();
        while (iterator.hasNext()) {
            Reason reason = iterator.next();
            String key = reason.getReason().replaceAll("\\*", "ast");
            reason.setReason(key);
            paste.getjSonReasons().add(key);
            if (listReasons.contains(key)) {
                continue;
            }
            if (mapReasonsFirst.containsKey(key)) {
                Reason value = mapReasonsFirst.remove(key);
                reason.setCorrects(reason.getCorrects() + value.getCorrects());
                reason.setIncorrects(reason.getIncorrects() + value.getIncorrects());
            }
            listReasons.add(key);
            String fileName = reasonDir + "\\" + key + ext;
            String fileData = gson.toJson(reason);
            writeToFile(fileName, fileData);
        }
    }

    private static void writeToFile(String fileName, String fileData) {
        try {
            FileWriter fw = new FileWriter(fileName);
            PrintWriter pw = new PrintWriter(fw);
            pw.print(fileData);
            if (pw != null) {
                pw.close();
            }
            if (fw != null) {
                fw.close();
            }
        } catch (Exception e) {
            System.out.println("Error creating file: " + fileName + ":\n" + e.toString());

        }
    }
}