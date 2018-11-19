package br.org.rnp.pastebinminerator.minner;

import java.net.ConnectException;
import java.util.ArrayList;

import org.jpaste.pastebin.Pastebin;
import org.jpaste.pastebin.PastebinLink;
import org.jpaste.pastebin.PastebinPaste;
import org.jpaste.pastebin.exceptions.ParseException;
import org.jpaste.utils.web.Post;

import br.org.rnp.pastebinminerator.dao.temporary.ParsedPasteKeyTemporarySetDAO;
import br.org.rnp.pastebinminerator.entity.json.Paste;
import br.org.rnp.pastebinminerator.util.Log;

/**
 * @author Felipe
 *
 *         Colects data from Pastebin.
 */
public class DataColector {

    public DataColector() {
        super();
    }

    public synchronized ArrayList<Paste> collectMostRecents(Integer numberOfPastes) throws ConnectException {
        if (numberOfPastes <= 0 || numberOfPastes > 250) {
            throw new IllegalArgumentException("The number of pastes must be between 1 and 250 inclusive.");
        }

        ArrayList<Paste> pastes = new ArrayList<Paste>();
        Post post = new Post();
        post.put("limit", numberOfPastes.toString());
        try {
            PastebinLink[] pastebinLinks = Pastebin.getMostRecent(post);
            for (PastebinLink pl : pastebinLinks) {
                if (!ParsedPasteKeyTemporarySetDAO.getInstance().contains(pl.getKey())) {
                    try {
                        Paste paste = convertePastebinLinkToPaste(pl);
                        if (paste != null) {
                            pastes.add(paste);
                            ParsedPasteKeyTemporarySetDAO.getInstance().add(paste.getKey());
                        }
                    } catch (RuntimeException e1) {
                        Log.log(e1.toString(), this.getClass().getName());
                    }
                }
            }
        } catch (RuntimeException e2) {
            Log.log(e2.toString(), this.getClass().getName());
        } catch (ParseException e) {
            Log.log(e.toString(), this.getClass().getName());
        } 

        return pastes;
    }

    private Paste convertePastebinLinkToPaste(PastebinLink pl) {
        Paste paste = null;

        pl.fetchContent();
        PastebinPaste pp = pl.getPaste();

        paste = new Paste();
        paste.setKey(pl.getKey());
        paste.setHits(pl.getHits());
        paste.setPasteDate(pl.getPasteDate());
        paste.setFormat(pp.getPasteFormat());
        paste.setEspireDate(pp.getPasteExpireDate().getValue());
        paste.setTitle(pp.getPasteTitle());
        paste.setVisibility(pp.getVisibility());
        paste.setText(pp.getContents());

        // System.out.println(pp.getAccount().getUsername() + " " +
        // pp.getAccount().getDeveloperKey());// +
        // "\n"
        // +
        // pp.getAccount().getAccountDetails().toString());
        return paste;
    }
}