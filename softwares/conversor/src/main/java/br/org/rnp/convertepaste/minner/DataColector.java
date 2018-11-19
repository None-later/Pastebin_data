package br.org.rnp.convertepaste.minner;

import java.util.List;

import br.org.rnp.convertepaste.dao.persistent.PasteJpaDAO;
import br.org.rnp.convertepaste.entity.jpa.Paste;
import br.org.rnp.convertepaste.util.Log;

/**
 * @author Felipe
 *
 *         Colects data from Pastebin.
 */
public class DataColector {

    public DataColector() {
        super();
    }

    public synchronized List<Paste> collectMostRecents(Integer numberOfPastes)  {
        List<Paste> pastes = null;
        
        try {
            pastes = PasteJpaDAO.getInstance().getPasteAmount(numberOfPastes);
        } catch (Exception e) {
            Log.log("Erro carregando pastes mais recentes: " + e.toString());
        }
        return pastes;
    }

}