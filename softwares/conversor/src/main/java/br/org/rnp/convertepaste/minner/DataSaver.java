package br.org.rnp.convertepaste.minner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.org.rnp.convertepaste.dao.persistent.FullPasteJpaDAO;
import br.org.rnp.convertepaste.dao.persistent.PasteJpaDAO;
import br.org.rnp.convertepaste.entity.jpa.FullPaste;
import br.org.rnp.convertepaste.entity.jpa.Paste;
import br.org.rnp.convertepaste.util.Log;

public class DataSaver {
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


        public void processPaste(Paste paste) {
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
                PasteJpaDAO.getInstance().remove(paste);
            }
        } catch (Exception e) {
            Log.log("Error saving paste: key = " + paste.getKey() + "\n" + e.toString());
        }
    }
}