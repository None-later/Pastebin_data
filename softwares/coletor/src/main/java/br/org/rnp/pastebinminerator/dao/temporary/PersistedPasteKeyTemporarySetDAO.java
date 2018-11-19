package br.org.rnp.pastebinminerator.dao.temporary;

public class PersistedPasteKeyTemporarySetDAO extends PasteKeyTemporarySetDAO {
    private static PersistedPasteKeyTemporarySetDAO instance;

    private PersistedPasteKeyTemporarySetDAO() {
        super();
    }

    public static synchronized PersistedPasteKeyTemporarySetDAO getInstance() {
        if (instance == null) {
            instance = new PersistedPasteKeyTemporarySetDAO();
        }
        return instance;
    }
}