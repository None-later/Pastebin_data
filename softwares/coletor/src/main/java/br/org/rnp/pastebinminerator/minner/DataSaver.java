package br.org.rnp.pastebinminerator.minner;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.org.rnp.pastebinminerator.dao.persistent.FullPasteJpaDAO;
import br.org.rnp.pastebinminerator.entity.jpa.FullPaste;
import br.org.rnp.pastebinminerator.entity.json.Paste;
import br.org.rnp.pastebinminerator.util.Log;

public class DataSaver implements Runnable {
    private LinkedList<Paste> queue = new LinkedList<Paste>();
    private boolean running = true;
    private static DataSaver instance = null;
    private Gson gson;

    public static DataSaver getInstance() {
        if (instance == null) {
            instance = new DataSaver();
        }
        return instance;
    }

    private DataSaver() {
        super();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public synchronized boolean add(Paste paste) {
        return this.queue.add(paste);
    }

    public synchronized boolean add(List<Paste> pastes) {
        return this.queue.addAll(pastes);
    }

    public void run() {
        while (running) {
            if (this.queue.size() > 0) {
                // System.out.println("#saving:" + this.queue.size());
                Paste paste = queue.removeFirst();
                processPaste(paste);
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Log.log(e.toString());
                }
            }
        }
    }

    private void processPaste(Paste paste) {
        if (paste == null) {
            Log.log("Paste can't be null!");
            return;
        }
        // System.out.println("#Saving paste");
        FullPaste savedPaste = null;
        try {
            String text = gson.toJson(paste);
            if (text != null && !text.isEmpty()) {
                savedPaste = FullPasteJpaDAO.getInstance().save(new FullPaste(text, paste.getKey(), false));
            }
        } catch (Exception e) {
            Log.log("Error saving paste: key = " + paste.getKey() + "\n" + e.toString());
        }
        if (savedPaste == null) {
            this.add(paste);
        }
    }
}